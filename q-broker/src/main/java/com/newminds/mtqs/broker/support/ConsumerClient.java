package com.newminds.mtqs.broker.support;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.JobResponse;
import com.newminds.mtqs.common.job.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 12/09/20
 **/
@Slf4j
public class ConsumerClient {

  public static JobResponse pushJobToConsumer(ConsumerInfo consumerInfo, SimpleJob simpleJob) {
    //Push a job to the consumer
    Mono<JobResponse> response = WebClient.create(consumerInfo.getJobUrl())
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(simpleJob))
            .retrieve()
            .bodyToMono(JobResponse.class);

    //If a job is not accepted by the consumer find the next best consumer to push the job to
    JobResponse jobResponse = response.block();
    log.info("Job Response, {}", jobResponse);
    return jobResponse;
  }
}
