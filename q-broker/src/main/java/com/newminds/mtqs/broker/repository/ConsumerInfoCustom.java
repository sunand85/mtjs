package com.newminds.mtqs.broker.repository;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;

/**
 * Created by Sunand on 10/09/20
 **/
public interface ConsumerInfoCustom {

  ReactiveMongoTemplate getReactiveMongoTemplate();
}
