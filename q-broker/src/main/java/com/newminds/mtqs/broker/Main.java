package com.newminds.mtqs.broker;

import com.newminds.mtqs.broker.repository.SimpleJobRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.common.job.Header;
import com.newminds.mtqs.common.job.Job;
import com.newminds.mtqs.common.job.JobBuilder;
import com.newminds.mtqs.common.topic.Topic;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Created by Sunand on 27/08/2020.
 **/
@SpringBootApplication
@EnableWebFlux
@EnableReactiveMongoRepositories
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
    System.out.println("Broker Started");
  }

  @Autowired
  private SimpleJobRepository jobRepository;
  @Autowired
  private TopicRepository topicRepository;

//  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    Topic topic = Topic.builder().name("event").durable(true).ordered(true).build();
    topicRepository.save(topic).subscribe();

    Job simpleJob = JobBuilder.asNewJob()
            .name("sunand")
            .header(Header.builder().tenantId("123").tenantType("demo").source("local").userId("abc").topic("event").now(DateTime.now()).build())
            .payload("Job1")
//            .type(JobType.SIMPLE)
            .build();

    jobRepository.save(simpleJob).subscribe();

    System.out.println("I did my job");
  }
}
