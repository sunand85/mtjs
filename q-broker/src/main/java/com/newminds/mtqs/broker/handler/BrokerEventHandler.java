package com.newminds.mtqs.broker.handler;

import com.newminds.mtqs.broker.event.ConsumerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 09/09/20
 **/
@Slf4j
@Service
public class BrokerEventHandler {


  @EventListener
  public void newConsumerRegistered(ConsumerCreatedEvent consumerCreatedEvent) {
    //Start pushing jobs to this new consumer if - *********

  }
}
