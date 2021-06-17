package com.whereq.campsite.service;

import com.whereq.campsite.domain.Campsite;
import com.whereq.campsite.domain.CampsiteAvailability;
import com.whereq.campsite.domain.CampsiteReservation;
import com.whereq.campsite.domain.User;
import com.whereq.campsite.exception.CampsiteReservationException;
import com.whereq.campsite.model.CampsiteVO;
import com.whereq.campsite.model.event.CampsiteEvent;
import com.whereq.campsite.repository.CampsiteAvailabilityRepository;
import com.whereq.campsite.repository.CampsiteRepository;
import com.whereq.campsite.repository.CampsiteReservationRepository;
import com.whereq.campsite.utils.CampsiteUtils;
import com.whereq.campsite.utils.Constants;
import com.whereq.campsite.utils.DateConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CampsiteService {

  @Autowired
  CampsiteRepository campsiteRepository;

  @Autowired
  CampsiteAvailabilityRepository campsiteAvailabilityRepository;

  @Autowired
  CampsiteReservationRepository campsiteReservationRepository;

  private CampsiteReservation validateInput(User user, Campsite campsite, Integer startDateInNum, Integer span) {
    if (span < 1) {
      throw new CampsiteReservationException(String.format("The campsite can't be reserved for less than %d days.", 1));
    }

    if (span > Constants.MAX_RESERVATION_DAYS) {
      throw new CampsiteReservationException(String.format("The campsite can not be reserved for more than %d days", Constants.MAX_RESERVATION_DAYS));
    }

    int startDayOfYear = DateConverter.integer2LocalDate(startDateInNum).getDayOfYear();
    int currentDayOfYear = LocalDate.now().getDayOfYear();
    if ((startDayOfYear - currentDayOfYear < 1) || (startDayOfYear - currentDayOfYear > 30)) {
      throw new CampsiteReservationException("The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.");
    }

    LocalDate startDate = DateConverter.integer2LocalDate(startDateInNum);
    Integer endDateInNum = DateConverter.localDate2Integer(startDate.plusDays(span));

    CampsiteReservation campsiteReservation = new CampsiteReservation();
    campsiteReservation.setCampsite(campsite);
    campsiteReservation.setUser(user);
    campsiteReservation.setStartDate(startDateInNum);
    campsiteReservation.setEndDate(endDateInNum);
    return campsiteReservation;
  }

  @Transactional
  public CampsiteVO createCampsite(CampsiteVO campsiteVO) {
    Campsite campsite = new Campsite();
    BeanUtils.copyProperties(campsiteVO, campsite);
    campsite = campsiteRepository.save(campsite);
    campsiteVO.setId(campsite.getId());
    return campsiteVO;
  }

  @Transactional(readOnly = true)
  public Campsite getCampsite(Long id) {
    return campsiteRepository.findById(id).orElse(null);
  }

  @Transactional(readOnly = true)
  public CampsiteVO findCampsiteAvailability(Long campsiteId, Integer startDateInNum, Integer span) {
    CampsiteAvailability campsiteAvailability = campsiteAvailabilityRepository.findByCampsiteId(campsiteId);
    return CampsiteUtils.campsiteAvailability2VO(campsiteAvailability, startDateInNum, span);
  }

  @Transactional(readOnly = true)
  public List<CampsiteVO> findAvailableCampsites(Integer startDateInNum, Integer span) {
    List<CampsiteAvailability> campsiteAvailabilityList = campsiteAvailabilityRepository.findAll();
    if (campsiteAvailabilityList == null || campsiteAvailabilityList.size() == 0) {
      return Collections.emptyList();
    }

    List<CampsiteVO> campsiteVOList = new ArrayList<>();
    for (CampsiteAvailability campsiteAvailability : campsiteAvailabilityList) {
      CampsiteVO campsiteVO = CampsiteUtils.campsiteAvailability2VO(campsiteAvailability, startDateInNum, span);
      if (campsiteVO != null) {
        campsiteVOList.add(campsiteVO);
      }
    }
    return campsiteVOList;
  }

  @Transactional(readOnly = true)
  public CampsiteReservation getCampsiteReservation(Long id) {
    return campsiteReservationRepository.findById(id).orElse(null);
  }

  @Transactional(readOnly = true)
  public List<CampsiteReservation> findCampsiteReservation(User user, Campsite campsite, Integer startDateInNum, Integer span) {
    CampsiteReservation campsiteReservation = validateInput(user, campsite, startDateInNum, span);
    List<CampsiteReservation> campsiteReservationList = campsiteReservationRepository.findCampsiteByDate(campsite.getId(), campsiteReservation.getStartDate(), campsiteReservation.getEndDate());
    return campsiteReservationList;
  }

  /**
   * @param user
   * @param campsite
   * @param startDateInNum
   * @param span
   * @return
   * @throws Exception
   */
  @Transactional
  public CampsiteReservation reserveCampsite(User user, Campsite campsite, Integer startDateInNum, Integer span) {
    CampsiteReservation campsiteReservation = validateInput(user, campsite, startDateInNum, span);

    CampsiteVO campsiteVO = findCampsiteAvailability(campsite.getId(), startDateInNum, span);
    if (campsiteVO != null) { // The campsite is available for the given date period
      CampsiteAvailability campsiteAvailability = campsiteAvailabilityRepository.findByCampsiteId(campsite.getId());
      campsiteAvailability.setAvailability(CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability.getAvailability(), startDateInNum, span, '1'));
      campsiteAvailability.setUpdatedTimestamp(LocalDateTime.now());
      campsiteAvailabilityRepository.save(campsiteAvailability);

      campsiteReservation.setUpdatedTimestamp(LocalDateTime.now());
      return campsiteReservationRepository.save(campsiteReservation);
    }
    return null;
  }


  /**
   * @param campsiteReservationId
   * @param user
   * @param campsite
   * @param startDateInNum
   * @param span
   * @return
   * @throws Exception
   */
  @Transactional
  public CampsiteReservation reserveCampsite(Long campsiteReservationId, User user, Campsite campsite, Integer startDateInNum, Integer span) {
    CampsiteReservation campsiteReservation = validateInput(user, campsite, startDateInNum, span);
    campsiteReservation.setId(campsiteReservationId);

    CampsiteVO campsiteVO = findCampsiteAvailability(campsite.getId(), startDateInNum, span);
    if (campsiteVO != null) { // The campsite is available for the given date period
      CampsiteAvailability campsiteAvailability = campsiteAvailabilityRepository.findByCampsiteId(campsite.getId());
      campsiteAvailability.setAvailability(CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability.getAvailability(), startDateInNum, span, '1'));
      campsiteAvailability.setUpdatedTimestamp(LocalDateTime.now());
      campsiteAvailabilityRepository.save(campsiteAvailability);

      campsiteReservation.setUpdatedTimestamp(LocalDateTime.now());
      return campsiteReservationRepository.save(campsiteReservation);
    }
    return null;
  }

  /**
   * Modify campsite reservation based on the assumption that when the user is trying to modify the reservation,
   * there will be no any conflicts with other existing reservations, since the target campsite and start/end date will be
   * selected from front-end, and only available slots will be shown in front-end.
   * @param campsiteEvent
   * @return
   */
  @Transactional
  public CampsiteReservation modifyCampsiteReservation(CampsiteEvent campsiteEvent) {
    // Revert the existing reservation
    if (cancelCampsiteReservation(campsiteEvent.getReservationId()) == null) {
      throw new CampsiteReservationException("Failed to modify the reservation since the given reservation doesn't exist.");
    }

    User user = new User();
    Campsite campsite = new Campsite();

    user.setId(campsiteEvent.getUserId());
    campsite.setId(campsiteEvent.getCampsiteId());

    return reserveCampsite(campsiteEvent.getReservationId(), user, campsite, campsiteEvent.getStartDate(), campsiteEvent.getSpan());
  }

  @Transactional
  public CampsiteReservation cancelCampsiteReservation(Long id) {
    CampsiteReservation campsiteReservation = campsiteReservationRepository.getById(id);
    if (campsiteReservation == null) {
      return null;
    }

    Integer startDateInNum = campsiteReservation.getStartDate();
    Integer endDateInNum = campsiteReservation.getEndDate();
    Integer span =
        Math.abs(Integer.parseInt(Long.toString(ChronoUnit.DAYS.between(DateConverter.integer2LocalDate(startDateInNum),
                                                                        DateConverter.integer2LocalDate(endDateInNum)))));

    // Revert the availability
    CampsiteAvailability campsiteAvailability = campsiteAvailabilityRepository.findByCampsiteId(campsiteReservation.getCampsite().getId());
    campsiteAvailability.setAvailability(CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability.getAvailability(), startDateInNum, span, '0'));
    campsiteAvailability.setUpdatedTimestamp(LocalDateTime.now());
    campsiteAvailabilityRepository.save(campsiteAvailability);

    campsiteReservationRepository.deleteById(campsiteReservation.getId());
    return campsiteReservation;
  }

  @Transactional
  public void syncReservationAndAvailability() {
    List<CampsiteReservation> campsiteReservationList = campsiteReservationRepository.findAll();
    for (CampsiteReservation campsiteReservation : campsiteReservationList) {
      Integer startDateInNum = campsiteReservation.getStartDate();
      Integer endDateInNum = campsiteReservation.getEndDate();
      Integer span = Math.abs(Integer.parseInt(Long.toString(ChronoUnit.DAYS.between(DateConverter.integer2LocalDate(startDateInNum), DateConverter.integer2LocalDate(endDateInNum)))));

      CampsiteAvailability campsiteAvailability = campsiteAvailabilityRepository.findByCampsiteId(campsiteReservation.getCampsite().getId());
      campsiteAvailability.setAvailability(CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability.getAvailability(), startDateInNum, span, '1'));
      campsiteAvailability.setUpdatedTimestamp(LocalDateTime.now());
      campsiteAvailabilityRepository.save(campsiteAvailability);
    }
  }
}
