package com.newminds.mtqs.consumer.controller;

import com.newminds.mtqs.common.job.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Created by Sunand on 10/09/20
 **/
@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

  @PostMapping("/jobs")
  public Mono<JobResponse> acceptJob(@RequestBody SimpleJob job) {
    log.info("Received Request, {}", job.getId());
    return Mono.just(new JobResponse(job.getId(), UUID.randomUUID().toString(), JobStatus.STARTING));
  }

  @GetMapping("/jobs")
  public Mono<String> getJobs() {
    return Mono.just("Hello World");
  }
}
