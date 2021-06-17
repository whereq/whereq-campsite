package com.whereq.campsite.utils;

import com.whereq.campsite.enums.ActionTypeEnum;
import com.whereq.campsite.model.event.CampsiteEvent;

import java.time.LocalDateTime;

public class EventUtils {

  public static CampsiteEvent createCampsiteEvent(Long userId, Long campsiteId, Integer startDate, Integer span, ActionTypeEnum actionTypeEnum) {
    CampsiteEvent campsiteEvent = new CampsiteEvent();
    campsiteEvent.setSeq(SequenceGenerator.next());
    campsiteEvent.setUserId(userId);
    campsiteEvent.setCampsiteId(campsiteId);
    campsiteEvent.setStartDate(startDate);
    campsiteEvent.setSpan(span);
    campsiteEvent.setAction(actionTypeEnum.toString());
    campsiteEvent.setEventTimestamp(LocalDateTime.now());
    return campsiteEvent;
  }
}
