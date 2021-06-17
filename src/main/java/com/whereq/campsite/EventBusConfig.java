package com.whereq.campsite;

import com.google.common.eventbus.EventBus;
import com.whereq.campsite.model.event.CampsiteEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class EventBusConfig {

  public final static ConcurrentHashMap<Long, CampsiteEvent> CAMPSITE_EVENT_QUEUE =
      new ConcurrentHashMap<>(16, 0.9f, 16);

  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
}
