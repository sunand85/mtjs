package com.newminds.mtqs.broker.repository;

import com.newminds.mtqs.common.topic.Topic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 07/09/20
 **/
@Repository
public interface TopicRepository extends ReactiveMongoRepository<Topic, String> {
  /**
   * Find Topic by Name
   * @param name
   * @return
   */
  Mono<Topic> findByName(String name);
}
