package com.newminds.mtqs.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.Job;
import com.newminds.mtqs.consumer.config.ConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sunand on 04/09/20
 **/
@Slf4j
@SpringBootApplication
@EnableWebFlux
@EnableScheduling
@EnableReactiveMongoRepositories
public class ConsumerAppStarter {

  private static ConsumerConfig consumerConfig;

  public static void main(String[] args) throws IOException, InterruptedException {
    loadConsumerConfig();
    SpringApplication application = new SpringApplication(ConsumerAppStarter.class);
//    application.setWebApplicationType(WebApplicationType.NONE);
    application.setDefaultProperties(Collections.singletonMap("server.port", consumerConfig.getPort()));
    application.run(args);

    bootstrap();
  }

  private static void loadConsumerConfig() throws IOException {
    File file = new File("./config/consumer.json");
    if(file.exists()) {
      String consumerJson = FileUtils.readFileToString(file);
      ObjectMapper objectMapper = new ObjectMapper();
      consumerConfig = objectMapper.readValue(file, ConsumerConfig.class);
    } else {
      log.error("Please provide consumer.json file for me to start");
      System.exit(1);
    }
  }

  //Start reading all jobs assigned to this consumer
  private static void bootstrap() throws InterruptedException {
    //Register Consumer to Broker
    WebClient webClient = WebClient.create(consumerConfig.getBrokerUrl() + "/consumers");
    ConsumerInfo consumerInfo = prepareConsumerInfo(consumerConfig);
    Mono<ConsumerInfo> consumerInfoMono = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(consumerInfo))
            .retrieve()
            .bodyToMono(ConsumerInfo.class);
    Mono<ConsumerInfo> response = consumerInfoMono.single().switchIfEmpty(Mono.error(new RuntimeException("Registration Failed")));
    log.info("Registered " + response.blockOptional().orElseThrow(() -> new RuntimeException("Response Came as EMPTY")));

    //Now each consumer will create a DAG on the incoming jobs to decide on what can be executed in parallel
    //Keep polling for jobs and update the DAG
    //Once the Job is done remove from the DAG

    //In order to pick jobs from the DAG or TREE or whatever we can apply various scheduling algorithms
    //We could use simple FCFS or We could use RoundRobin on Tenant Wise Job

    //Have a max job this consumer can pick and based on that pull the job from DB or Broker
//    poll();
  }

  private static ConsumerInfo prepareConsumerInfo(ConsumerConfig consumerConfig) {
    ConsumerInfo consumerInfo = new ConsumerInfo();
    String id = "CONN_" + (consumerConfig.getId() != null ? consumerConfig.getId() : DateTime.now().getMillis() + "_" + ManagementFactory.getRuntimeMXBean().getName());
    consumerInfo.setId(id);
    consumerInfo.setName(consumerConfig.getName());
    consumerInfo.setHost(InetAddress.getLoopbackAddress().getHostName());
    consumerInfo.setPort(consumerConfig.getPort());
    consumerInfo.setAlive(true);
//    consumerInfo.setTopic(consumerConfig.getTopics().get(0)); //Hard-coding to one topic
    return consumerInfo;
  }

  private static void poll() throws InterruptedException {
    while(true) {
      fetchJobs();
      //Poll every X millis
//      Thread.sleep(brokerInfo.getPoll());
      Thread.sleep(1_000);
    }
  }

  private static List<Job> fetchJobs() {
//    broker.fet
    return null;
  }
}
