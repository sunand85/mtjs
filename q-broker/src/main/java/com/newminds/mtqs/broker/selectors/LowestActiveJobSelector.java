package com.newminds.mtqs.broker.selectors;

import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.TopicConsumerRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.topic.Topic;
import com.newminds.mtqs.common.topic.TopicConsumerLookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Sunand on 12/09/20
 **/
@Slf4j
@Service
public class LowestActiveJobSelector extends AbstractSelector {

  public LowestActiveJobSelector(ConsumerInfoRepository consumerInfoRepository, TopicRepository topicRepository, TopicConsumerRepository topicConsumerRepository) {
    super(consumerInfoRepository, topicRepository, topicConsumerRepository);
  }

  //This is kind of work stealing algorithm
  @Override
  public ConsumerInfo select(Topic topic) {
    if(topic.isMultiTenant()) {

      Flux<TopicConsumerLookup> consumers = topicConsumerRepository.findByTopicNameAndTenantId(topic.getName(), topic.getTenantId());
      List<String> consumerIdList = consumers
              .map(tcl -> tcl.getConsumerId())
              .collectList().block();

      Criteria criteria = Criteria.where("id").in(consumerIdList);
      Query query = new Query(criteria)
              .with(Sort.by(Sort.Direction.ASC, "activeJobs"))
              .limit(1);

      Mono<ConsumerInfo> selectedConsumer = consumerInfoRepository.getReactiveMongoTemplate().findOne(query, ConsumerInfo.class);

      ConsumerInfo consumerInfo = selectedConsumer.blockOptional().orElseGet(() -> pickNewConsumerForThisJob(topic.getName(), topic.getTenantId()));
      log.info("Selected Consumer Details, {}", consumerInfo);
      return consumerInfo;
    }
    return null;
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
