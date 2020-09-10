package com.newminds.mtqs.broker.controller;

import com.newminds.mtqs.broker.event.JobCreatedEvent;
import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.SimpleJobRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.broker.service.ConsumerInfoService;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.Job;
import com.newminds.mtqs.common.job.SimpleJob;
import com.newminds.mtqs.common.job.Topic;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 07/09/20
 **/
@RestController
@RequestMapping("/broker")
public class BrokerController {

  private final SimpleJobRepository jobRepository;
  private final TopicRepository topicRepository;
  private final ConsumerInfoRepository consumerInfoRepository;

  private final ConsumerInfoService consumerInfoService;
  private final ApplicationEventPublisher eventPublisher;

  public BrokerController(SimpleJobRepository jobRepository, TopicRepository topicRepository, ConsumerInfoRepository consumerInfoRepository, ConsumerInfoService consumerInfoService, ApplicationEventPublisher eventPublisher) {
    this.jobRepository = jobRepository;
    this.topicRepository = topicRepository;
    this.consumerInfoRepository = consumerInfoRepository;
    this.consumerInfoService = consumerInfoService;
    this.eventPublisher = eventPublisher;
  }

  //CONSUMER REGISTRY API
  @GetMapping("/consumers")
  public Flux<ConsumerInfo> getAllConsumerInfo() {
    return consumerInfoService.findAll();
  }

  @PostMapping(value = "/consumers", consumes = "application/json")
  public Mono<ConsumerInfo> addConsumer(@RequestBody ConsumerInfo consumerInfo) {
    System.out.println(consumerInfo);
//    return consumerInfoRepository.save(consumerInfo);
    return consumerInfoService.save(consumerInfo);
  }

  //The below is TBD since run time consumer properties refresh need to be thought through
  @PutMapping("/consumers/{id}")
  public Mono<ConsumerInfo> updateConsumer(String id, @RequestBody ConsumerInfo consumerInfo) {
    consumerInfo.setId(id);
    return consumerInfoService.update(consumerInfo);
  }

  @DeleteMapping("/consumers/{id}")
  public Mono<Void> removeConsumer(@PathVariable String id) {
    return consumerInfoRepository.deleteById(id);
  }

  @DeleteMapping("/consumers")
  public Mono<Mono<Object>> removeAllConsumers() {
    return consumerInfoRepository.deleteAll().thenReturn(Mono.empty());
  }

  //TOPICS API
  @GetMapping("/topics")
  public Flux<Topic> getAllTopics() {
    return topicRepository.findAll();
  }

  @PostMapping("/topics")
  public Mono<Topic> createTopic(@RequestBody Topic topic) {
    return topicRepository.save(topic);
  }

  @GetMapping("/topics/{id}")
  public Mono<Topic> updateTopic(@PathVariable String id, @RequestBody Topic topic) {
    topic.setId(id);
    return topicRepository.save(topic);
  }

  @DeleteMapping("/topics/{id}")
  public Mono<Void> deleteTopic(@PathVariable String id) {
    return topicRepository.deleteById(id);
  }

  @DeleteMapping("/topics")
  public Mono<Void> deleteAllTopics() {
    return topicRepository.deleteAll().then(Mono.empty());
  }

  //JOBS API
  @GetMapping("/jobs")
  public Flux<Job> getAllJobs() {
    return jobRepository.findAll();
  }

  @GetMapping("/jobs/{id}")
  public Mono<Job> getJob(@PathVariable("id") String jobId) {
    return jobRepository.findById(jobId);
  }

  @PostMapping("/jobs")
  public Mono<Job> addJob(@RequestBody SimpleJob job) {

    Mono<Job> simpleJob = jobRepository.save(job);
    eventPublisher.publishEvent(new JobCreatedEvent(this, simpleJob));
    return simpleJob;
  }

  @PutMapping("/jobs/{id}")
  public Mono<Job> updateJob(@PathVariable("id") String jobId, @RequestBody SimpleJob job) {
    job.setId(jobId);
    return jobRepository.save(job);
  }

  @DeleteMapping("/jobs/{id}")
  public Mono<Void> removeJob(@PathVariable("id") String jobId) {
    return jobRepository.deleteById(jobId);
  }

  @DeleteMapping("/jobs")
  public Mono<Void> removeJobs() {
    ResponseEntity<Object> responseEntity = ResponseEntity.notFound().build();
    return jobRepository.deleteAll().then(Mono.empty());
  }
}
