package com.newminds.mtqs.broker.handler;

import com.newminds.mtqs.broker.event.ConsumerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 08/09/20
 **/
@Slf4j
@Service
public class ConsumerEventHandler {

  @EventListener
  public void consumerCreated(ConsumerCreatedEvent consumerCreatedEvent) {
    log.info("Consumer Created");
    log.info("Consumer Info : {}", consumerCreatedEvent.getInfo());
    log.info("Created Timestamp : {}", consumerCreatedEvent.getTimestamp());

    //Start Assigning Work to Consumer if the Topic has some incoming job
    //Raise another event to Broker so that it can see what jobs can be pushed to the newly added consumer
  }
}
