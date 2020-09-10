package com.newminds.mtqs.broker.repository;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 07/09/20
 **/
@Repository
public interface ConsumerInfoRepository extends ReactiveMongoRepository<ConsumerInfo, String>, ConsumerInfoCustom {
}
