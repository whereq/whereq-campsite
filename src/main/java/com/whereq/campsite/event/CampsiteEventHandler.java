package com.whereq.campsite.event;

import com.google.common.eventbus.EventBus;
import com.whereq.campsite.model.event.CampsiteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class CampsiteEventHandler {

  @Autowired
  EventBus eventBus;

  @Autowired
  CampsiteEventListener campsiteEventListener;

  @PostConstruct
  public void init() {
    eventBus.register(campsiteEventListener);
  }

  @PreDestroy
  public void destory() {
    eventBus.unregister(campsiteEventListener);
  }


  public void postEvent(CampsiteEvent campsiteEvent) {
    eventBus.post(campsiteEvent);
  }
}
