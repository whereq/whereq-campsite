package com.whereq.campsite.web.rest;

import com.whereq.campsite.async.AsyncEventHandler;
import com.whereq.campsite.domain.Campsite;
import com.whereq.campsite.domain.CampsiteReservation;
import com.whereq.campsite.domain.User;
import com.whereq.campsite.enums.ActionStatusEnum;
import com.whereq.campsite.enums.ActionTypeEnum;
import com.whereq.campsite.exception.CampsiteNotExistsException;
import com.whereq.campsite.exception.CampsiteReservationException;
import com.whereq.campsite.exception.GeneralValidationException;
import com.whereq.campsite.model.CampsiteReservationRequest;
import com.whereq.campsite.model.CampsiteReservationResponse;
import com.whereq.campsite.model.CampsiteVO;
import com.whereq.campsite.model.event.CampsiteEvent;
import com.whereq.campsite.service.CampsiteService;
import com.whereq.campsite.service.UserService;
import com.whereq.campsite.utils.CampsiteUtils;
import com.whereq.campsite.utils.DateConverter;
import com.whereq.campsite.utils.EventUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@Api(value = "Campsite")
public class CampsiteResource {

  @Autowired
  CampsiteService campsiteService;

  @Autowired
  AsyncEventHandler asyncEventHandler;

  @Autowired
  UserService userService;

  @GetMapping(value = {"/available/campsites"})
  public ResponseEntity<List<CampsiteVO>> findAvailableCampsite(@RequestParam(value = "startDate", required = false) Integer startDate,
                                                                @RequestParam(defaultValue = "30") Integer span) {

    Integer startDateInNum = (startDate == null) ? DateConverter.localDate2Integer(LocalDate.now()) : startDate;
    List<CampsiteVO> campsiteVOCollection = campsiteService.findAvailableCampsites(startDateInNum, span);
    return new ResponseEntity<>(campsiteVOCollection, HttpStatus.OK);
  }

  @GetMapping(value = {"/available/campsites/{id}"})
  public ResponseEntity<CampsiteVO> findAvailableCampsite(@PathVariable(value = "id") Long id,
                                                          @RequestParam(value = "startDate", required = false) Integer startDate,
                                                          @RequestParam(defaultValue = "30") Integer span) {

    Integer startDateInNum = (startDate == null) ? DateConverter.localDate2Integer(LocalDate.now()) : startDate;
    CampsiteVO campsiteVO = campsiteService.findCampsiteAvailability(id, startDateInNum, span);
    return new ResponseEntity<>(campsiteVO, HttpStatus.OK);
  }

  @PostMapping("/campsites")
  public ResponseEntity<CampsiteVO> createCampsite(@RequestBody CampsiteVO campsite) throws URISyntaxException {
    if (campsite.getId() != null) {
      throw new GeneralValidationException("A new campsite cannot already have an ID.");
    } else {
      campsite = campsiteService.createCampsite(campsite);
      return ResponseEntity.created(new URI("/api/campsites" + campsite.getId()))
          .body(campsite);
    }
  }

  /**
   * Reserve campsite
   *
   * @param campsiteReservationRequest
   * @return
   */
  @PostMapping("/campsite/reservation")
  public ResponseEntity<CampsiteReservationResponse> reserveCampsite(@RequestBody CampsiteReservationRequest campsiteReservationRequest) {
    Campsite campsite = campsiteService.getCampsite(campsiteReservationRequest.getCampsiteId());
    if (campsite == null) {
      throw new CampsiteNotExistsException("The requested campsite does not exist.");
    }

    User user = userService.getUserByEmail(campsiteReservationRequest.getEmail());
    if (user == null) { // Add the new user to our campsite!
      user = new User();
      BeanUtils.copyProperties(campsiteReservationRequest, user);
      user = userService.createUser(user);
    }

    Integer span =
        Math.abs(Integer.parseInt(Long.toString(ChronoUnit.DAYS.between(DateConverter.integer2LocalDate(campsiteReservationRequest.getStartDateInNum()),
            DateConverter.integer2LocalDate(campsiteReservationRequest.getEndDateInNum())))));

    CampsiteEvent campsiteEvent =
        EventUtils.createCampsiteEvent(user.getId(), campsite.getId(), campsiteReservationRequest.getStartDateInNum(), span, ActionTypeEnum.Book);

    try {
      CompletableFuture<CampsiteEvent> completableFuture = asyncEventHandler.postCampsiteEventAsync(campsiteEvent);
      campsiteEvent = completableFuture.get();

      if (campsiteEvent.getStatus().equals(ActionStatusEnum.Fail)) {
        throw new CampsiteReservationException(campsiteEvent.getReason());
      } else {
        CampsiteReservationResponse campsiteReservationResponse = CampsiteUtils.campsiteReservation2Response(campsiteEvent.getCampsiteReservation());
        return new ResponseEntity<>(campsiteReservationResponse, HttpStatus.OK);
      }
    } catch (Exception e) {
      throw new CampsiteReservationException(e.getMessage());
    }
  }

  /**
   * Modify the campsite reservation.
   * What can be modified: The reserved campsite, the start/end date;
   * What can't be modified: The user who sent the original reservation request
   *
   * @param campsiteReservationRequest
   * @return
   */
  @PutMapping("/campsite/reservation")
  public ResponseEntity<CampsiteReservationResponse> modifyCampsiteReservation(@RequestBody CampsiteReservationRequest campsiteReservationRequest) {
    Long id = campsiteReservationRequest.getId();
    if (id == null) {
      throw new GeneralValidationException("The ID is mandatory to modify an existing campsite reservation.");
    }

    CampsiteReservation campsiteReservation = campsiteService.getCampsiteReservation(id);
    if (campsiteReservation == null) {
      throw new GeneralValidationException("The campsite reservation doesn't exist.");
    }

    Campsite campsite = campsiteService.getCampsite(campsiteReservationRequest.getCampsiteId());
    if (campsite == null) {
      throw new CampsiteNotExistsException("The requested campsite does not exist.");
    }

    Integer span =
        Math.abs(Integer.parseInt(Long.toString(ChronoUnit.DAYS.between(DateConverter.integer2LocalDate(campsiteReservationRequest.getStartDateInNum()),
            DateConverter.integer2LocalDate(campsiteReservationRequest.getEndDateInNum())))));

    CampsiteEvent campsiteEvent =
        EventUtils.createCampsiteEvent(campsiteReservation.getUser().getId(), campsite.getId(), campsiteReservationRequest.getStartDateInNum(), span, ActionTypeEnum.Modify);

    // Set the reservation id into campsite event
    campsiteEvent.setReservationId(id);

    try {
      CompletableFuture<CampsiteEvent> completableFuture = asyncEventHandler.postCampsiteEventAsync(campsiteEvent);
      campsiteEvent = completableFuture.get();

      if (campsiteEvent.getStatus().equals(ActionStatusEnum.Fail)) {
        throw new CampsiteReservationException(campsiteEvent.getReason());
      } else {
        CampsiteReservationResponse campsiteReservationResponse = CampsiteUtils.campsiteReservation2Response(campsiteEvent.getCampsiteReservation());
        return new ResponseEntity<>(campsiteReservationResponse, HttpStatus.OK);
      }
    } catch (Exception e) {
      throw new CampsiteReservationException(e.getMessage());
    }
  }

  @DeleteMapping("/campsite/reservation")
  public ResponseEntity<CampsiteReservationResponse> cancelCampsiteReservation(@RequestBody CampsiteReservationRequest campsiteReservationRequest) {
    Long id = campsiteReservationRequest.getId();
    if (id == null) {
      throw new GeneralValidationException("The ID is mandatory to cancel an existing campsite reservation.");
    }

    CampsiteReservation campsiteReservation = campsiteService.getCampsiteReservation(id);
    if (campsiteReservation == null) {
      throw new GeneralValidationException("The campsite reservation doesn't exist.");
    }

    Campsite campsite = campsiteService.getCampsite(campsiteReservationRequest.getCampsiteId());
    if (campsite == null) {
      throw new CampsiteNotExistsException("The requested campsite does not exist.");
    }

    CampsiteEvent campsiteEvent =
        EventUtils.createCampsiteEvent(campsiteReservation.getUser().getId(), null, null, null, ActionTypeEnum.Cancel);

    // Set the reservation id into campsite event
    campsiteEvent.setReservationId(id);

    try {
      CompletableFuture<CampsiteEvent> completableFuture = asyncEventHandler.postCampsiteEventAsync(campsiteEvent);
      campsiteEvent = completableFuture.get();

      if (campsiteEvent.getStatus().equals(ActionStatusEnum.Fail)) {
        throw new CampsiteReservationException(campsiteEvent.getReason());
      } else {
        CampsiteReservationResponse campsiteReservationResponse = CampsiteUtils.campsiteReservation2Response(campsiteEvent.getCampsiteReservation());
        return new ResponseEntity<>(campsiteReservationResponse, HttpStatus.OK);
      }
    } catch (Exception e) {
      throw new CampsiteReservationException(e.getMessage());
    }
  }
}
