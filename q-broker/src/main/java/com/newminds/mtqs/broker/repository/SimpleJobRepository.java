package com.newminds.mtqs.broker.repository;

import com.newminds.mtqs.common.job.Job;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sunand on 07/09/20
 **/
@Repository
public interface SimpleJobRepository extends ReactiveMongoRepository<Job, String> {
}
