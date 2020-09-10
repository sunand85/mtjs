package com.newminds.mtqs.broker.handler;

import com.newminds.mtqs.broker.event.JobCreatedEvent;
import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.TopicConsumerRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.broker.service.ConsumerInfoService;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.JobResponse;
import com.newminds.mtqs.common.job.SimpleJob;
import com.newminds.mtqs.common.job.Topic;
import com.newminds.mtqs.common.job.TopicConsumerLookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

  public JobEventHandler(ConsumerInfoService consumerInfoService, ConsumerInfoRepository consumerInfoRepository, TopicRepository topicRepository, TopicConsumerRepository topicConsumerRepository) {
    this.consumerInfoService = consumerInfoService;
    this.consumerInfoRepository = consumerInfoRepository;
    this.topicRepository = topicRepository;
    this.topicConsumerRepository = topicConsumerRepository;
  }

  @EventListener
  public void jobAdded(JobCreatedEvent jobCreatedEvent) {
    log.info("Job Created");
    log.info("Job Request, {}", jobCreatedEvent.getJob());
    log.info("Job Created Timestamp, {}", jobCreatedEvent.getTimestamp());

    SimpleJob simpleJob = (SimpleJob) jobCreatedEvent.getJob().block();
    String topic = simpleJob.getHeader().getTopic();
    String tenantId = simpleJob.getHeader().getTenantId();

    log.info("Topic Name : {}", topic);
    Mono<Topic> topicMono = topicRepository.findByName(topic);

    Flux<TopicConsumerLookup> consumers = topicConsumerRepository.findByTopicNameAndTenantId(topic, tenantId);
    /*Mono<TopicConsumerLookup> selectedConsumer = consumers
            .map(tcl -> tcl.getConsumerId())
            .collectList()
//            .reduce((tcl1, tcl2) -> getTopicConsumerLookup(tcl1, tcl2))
            .switchIfEmpty(pickNewConsumerForThisJob(topic, tenantId));*/
    List<String> consumerIdList = consumers
            .map(tcl -> tcl.getConsumerId())
            .collectList().block();

    Criteria criteria = Criteria.where("id").in(consumerIdList);
    Query query = new Query(criteria)
            .with(Sort.by(Sort.Direction.ASC, "activeJobs"))
            .limit(1);

    Mono<ConsumerInfo> selectedConsumer = consumerInfoRepository.getReactiveMongoTemplate().findOne(query, ConsumerInfo.class);

    //TODO: if a consumer has reached max threshold then again go and pick a new consumer

    ConsumerInfo consumerInfo = selectedConsumer.blockOptional().orElseGet(() -> pickNewConsumerForThisJob(topic, tenantId));
    log.info("Selected Consumer Details, {}", consumerInfo);

    //Push a job to the consumer
    Mono<JobResponse> response = WebClient.create(consumerInfo.getJobUrl())
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(simpleJob))
            .retrieve()
            .bodyToMono(JobResponse.class);

    //If a job is not accepted by the consumer find the next best consumer to push the job to
    log.info("Job Response, {}", response.block());
    log.info("Done");
  }

  /**
   * Choosing a consumer based on Number of Active Jobs
   *
   * @param tcl1
   * @param tcl2
   * @return
   */
  private TopicConsumerLookup getTopicConsumerLookup(TopicConsumerLookup tcl1, TopicConsumerLookup tcl2) {
    ConsumerInfo c1 = consumerInfoService.find(tcl1.getConsumerId()).block();
    tcl1.setConsumerInfo(c1);
    int activeJobs1 = c1.getStats().getActiveJobs();

    ConsumerInfo c2 = consumerInfoService.find(tcl2.getConsumerId()).block();
    tcl2.setConsumerInfo(c2);
    int activeJobs2 = c2.getStats().getActiveJobs();

    //Decision
    return activeJobs1 < activeJobs2 ? tcl1 : tcl2;
  }

  /**
   * Deciding a consumer based on active jobs alone. Later more complex strategies can be added here.
   *
   * @param topic
   * @param tenantId
   * @return
   */
  private ConsumerInfo pickNewConsumerForThisJob(String topic, String tenantId) {
    log.info("Selecting a new consumer");
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, "activeJobs"));
    query.limit(1);
    ConsumerInfo consumerInfo = consumerInfoRepository.getReactiveMongoTemplate().findOne(query, ConsumerInfo.class).block();
    TopicConsumerLookup consumerLookup = new TopicConsumerLookup();
    consumerLookup.setConsumerId(consumerInfo.getId());
    consumerLookup.setTopicName(topic);
    consumerLookup.setTenantId(tenantId);
    topicConsumerRepository.save(consumerLookup).subscribe();
    return consumerInfo;
  }
}
