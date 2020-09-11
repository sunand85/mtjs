package com.newminds.mtqs.broker.repository;

import com.newminds.mtqs.common.topic.TopicConsumerLookup;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Created by Sunand on 09/09/20
 **/
@Repository
public interface TopicConsumerRepository extends ReactiveMongoRepository<TopicConsumerLookup, String> {

  Flux<TopicConsumerLookup> findByTopicNameAndTenantId(String topicName, String tenantId);
}
