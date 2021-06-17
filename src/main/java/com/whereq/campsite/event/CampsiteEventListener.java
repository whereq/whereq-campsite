package com.whereq.campsite.event;

import com.google.common.eventbus.Subscribe;
import com.whereq.campsite.EventBusConfig;
import com.whereq.campsite.enums.ActionStatusEnum;
import com.whereq.campsite.enums.ActionTypeEnum;
import com.whereq.campsite.domain.Campsite;
import com.whereq.campsite.domain.CampsiteReservation;
import com.whereq.campsite.domain.User;
import com.whereq.campsite.model.event.CampsiteEvent;
import com.whereq.campsite.service.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampsiteEventListener {

  @Autowired
  CampsiteService campsiteService;

  @Subscribe
  public void handleCampsiteReservationEvent(CampsiteEvent campsiteEvent) throws Exception {
    User user = new User();
    Campsite campsite = new Campsite();

    user.setId(campsiteEvent.getUserId());
    campsite.setId(campsiteEvent.getCampsiteId());

    CampsiteReservation campsiteReservation = null;
    String message = null;
    if (ActionTypeEnum.Book.toString().equalsIgnoreCase(campsiteEvent.getAction())) {
      campsiteReservation = campsiteService.reserveCampsite(user, campsite, campsiteEvent.getStartDate(), campsiteEvent.getSpan());
      if (campsiteReservation == null) {
        message = String.format("This campsite is not available for the %d days start from %d", campsiteEvent.getSpan(), campsiteEvent.getStartDate());
      }
    } else if (ActionTypeEnum.Modify.toString().equalsIgnoreCase(campsiteEvent.getAction())) {
      campsiteReservation = campsiteService.modifyCampsiteReservation(campsiteEvent);
      if (campsiteReservation == null) {
        message = String.format("Failed to modify the reservation the %d days start from %d", campsiteEvent.getSpan(), campsiteEvent.getStartDate());
      }
    } else { // cancel
      campsiteReservation = campsiteService.cancelCampsiteReservation(campsiteEvent.getReservationId());
      if (campsiteReservation == null) {
        message = "Failed to cancel the reservation since the given reservation doesn't exist.";
      }
    }
    if (campsiteReservation != null) { // The campsite is available for the given date period
      campsiteEvent.setStatus(ActionStatusEnum.Success.toString());
    } else {
      campsiteEvent.setReason(message);
      campsiteEvent.setStatus(ActionStatusEnum.Fail.toString());
    }
    campsiteEvent.setCampsiteReservation(campsiteReservation);

    EventBusConfig.CAMPSITE_EVENT_QUEUE.put(campsiteEvent.getSeq(), campsiteEvent);
  }
}
