package com.newminds.mtqs.broker.handler;

import com.newminds.mtqs.broker.selectors.LowestActiveJobSelector;
import com.newminds.mtqs.broker.support.ConsumerClient;
import com.newminds.mtqs.broker.event.JobCreatedEvent;
import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.TopicConsumerRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.broker.service.ConsumerInfoService;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.SimpleJob;
import com.newminds.mtqs.common.topic.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 09/09/20
 **/
@Slf4j
@Service
public class JobEventHandler {

  private final ConsumerInfoService consumerInfoService;
  private final ConsumerInfoRepository consumerInfoRepository;
  private final TopicRepository topicRepository;
  private final TopicConsumerRepository topicConsumerRepository;
  private final LowestActiveJobSelector selector;

  public JobEventHandler(ConsumerInfoService consumerInfoService, ConsumerInfoRepository consumerInfoRepository,
                         TopicRepository topicRepository, TopicConsumerRepository topicConsumerRepository,
                         LowestActiveJobSelector selector) {
    this.consumerInfoService = consumerInfoService;
    this.consumerInfoRepository = consumerInfoRepository;
    this.topicRepository = topicRepository;
    this.topicConsumerRepository = topicConsumerRepository;
    this.selector = selector;
  }

  @EventListener
  public void jobAdded(JobCreatedEvent jobCreatedEvent) {
    log.info("Job Created");
    log.info("Job Request, {}", jobCreatedEvent.getJob());
    log.info("Job Created Timestamp, {}", jobCreatedEvent.getTimestamp());

    SimpleJob simpleJob = (SimpleJob) jobCreatedEvent.getJob().block();
    String topicName = simpleJob.getHeader().getTopic();
    String tenantId = simpleJob.getHeader().getTenantId();

    log.info("Topic Name : {}", topicName);

    Mono<Topic> topicByName = topicRepository.findByName(topicName);
    Topic topic = topicByName.block();
    topic.setTenantId(tenantId);
    ConsumerInfo consumerInfo = selector.select(topic);
    //TODO: if a consumer has reached max threshold then again go and pick a new consumer
    //Or If the consumer did not accept the job then we have to either pick a new consumer or report this error.
    //Or it can be kind of backpressure where we have to create a thread monitor to continuously poll the selected consumer if it can accept the job or report after a certain period of time
    ConsumerClient.pushJobToConsumer(consumerInfo, simpleJob);
    log.info("Done");
  }

}
