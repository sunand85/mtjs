package com.newminds.mtqs.broker.service;

import com.newminds.mtqs.broker.event.ConsumerCreatedEvent;
import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 08/09/20
 **/
@Service
public class ConsumerInfoService extends GenericService<ConsumerInfo> {

  private final ConsumerInfoRepository consumerInfoRepository;
  private final ApplicationEventPublisher eventPublisher;

  public ConsumerInfoService(ConsumerInfoRepository consumerInfoRepository, ApplicationEventPublisher eventPublisher) {
    this.consumerInfoRepository = consumerInfoRepository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public ReactiveMongoRepository getRepository() {
    return consumerInfoRepository;
  }

  @Override
  public Mono<ConsumerInfo> save(ConsumerInfo consumerInfo) {
    Mono<ConsumerInfo> consumerInfoMono = super.save(consumerInfo);
    //Raise an event to kick
    eventPublisher.publishEvent(new ConsumerCreatedEvent(this, consumerInfo));
    return consumerInfoMono;
  }
}
