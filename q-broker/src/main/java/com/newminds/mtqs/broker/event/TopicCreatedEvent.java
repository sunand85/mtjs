package com.newminds.mtqs.broker.event;

import com.newminds.mtqs.common.topic.Topic;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Sunand on 09/09/20
 **/
@Getter
public class TopicCreatedEvent extends ApplicationEvent {

  private Topic topic;

  public TopicCreatedEvent(Object source, Topic topic) {
    super(source);
    this.topic = topic;
  }
}
