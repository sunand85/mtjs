package com.newminds.mtqs.broker.event;

import com.newminds.mtqs.common.job.Job;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 09/09/20
 **/
@Getter
public class JobCreatedEvent extends ApplicationEvent {

  private Mono<Job> job;

  public JobCreatedEvent(Object source, Mono<Job> job) {
    super(source);
    this.job = job;
  }
}
