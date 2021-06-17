package com.whereq.campsite.async;

import com.whereq.campsite.EventBusConfig;
import com.whereq.campsite.event.CampsiteEventHandler;
import com.whereq.campsite.model.event.CampsiteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncEventHandler {

  @Autowired
  CampsiteEventHandler campsiteEventHandler;

  /**
   * The thread sequence is:
   * Post Event ->
   * Event listener process the event ->
   * Push process result into a singleton queue in memory ->
   * The current thread wait on the queue to get the result ->
   * END
   *
   * Once posted the async event, the current thread will be waiting for the result being put into the CAMPSITE_EVENT_QUEUE.
   * This is intend for simplify the interface and cohere the sequence logic.
   * TODO: add timeout in the infinite loop to make sure the thread won't spin there forever if something went wrong!!!
   *
   * The other solution should be a fully async process for the campsite event cycle, once back-end received the campsite Book/Cancel/Modify request,
   * a CampsiteEvent object will be encapsulate and posted to the event bus to process, a response will be sent back to front-end right away to acknowledge the request,
   * front-end will be responsible for polling the campsite status from back-end.
   *
   * @param campsiteEvent
   * @return
   * @throws InterruptedException
   */
  @Async
  public CompletableFuture<CampsiteEvent> postCampsiteEventAsync(CampsiteEvent campsiteEvent) throws InterruptedException {

    Long seq = campsiteEvent.getSeq();
    campsiteEventHandler.postEvent(campsiteEvent);

    CampsiteEvent ce = null;
    boolean isResponseBack = false;
    while (!isResponseBack) { // TODO: A timeout condition checking should be added here in future!!!
      ce = EventBusConfig.CAMPSITE_EVENT_QUEUE.get(seq);
      if (ce != null) {
        isResponseBack = true;
      }
      Thread.sleep(1000L);
    }

    return CompletableFuture.completedFuture(ce);
  }

}
