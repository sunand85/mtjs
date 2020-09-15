package com.newminds.mtqs.consumer.controller;

import com.newminds.mtqs.common.job.*;
import com.newminds.mtqs.consumer.support.JobHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Created by Sunand on 10/09/20
 **/
@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

  private final JobHolder jobHolder;

  public ConsumerController(JobHolder jobHolder) {
    this.jobHolder = jobHolder;
  }

  @PostMapping("/jobs")
  public Mono<JobResponse> acceptJob(@RequestBody SimpleJob job) {
    log.info("Received Request, {}", job.getId());
    jobHolder.addJob(job);
    return Mono.just(new JobResponse(job.getId(), UUID.randomUUID().toString(), JobStatus.STARTING));
  }

  @GetMapping("/jobs")
  public Flux<SimpleJob> getJobs() {
    return Flux.fromStream(jobHolder.getJobs());
  }
}
