package com.newminds.mtqs.broker.event;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 08/09/20
 **/
@Getter
public class ConsumerCreatedEvent extends ApplicationEvent {

  private ConsumerInfo info;

  public ConsumerCreatedEvent(Object source, ConsumerInfo info) {
    super(source);
    this.info = info;
  }
}
