package com.newminds.mtqs.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newminds.mtqs.common.consumer.BrokerInfo;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.job.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Sunand on 04/09/20
 **/
@Slf4j
@SpringBootApplication
@EnableReactiveMongoRepositories
public class Consumer implements CommandLineRunner {

  private static ConsumerConfig consumerConfig;

  public static void main(String[] args) throws IOException, InterruptedException {
    SpringApplication application = new SpringApplication(Consumer.class);
    application.setWebApplicationType(WebApplicationType.NONE);
    application.run(args);

    loadConsumerConfig();
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
  private static void bootstrap() throws UnknownHostException, InterruptedException {
    //Register Consumer to Broker
    WebClient webClient = WebClient.create(consumerConfig.getBrokerUrl() + "/consumers");
    ConsumerInfo consumerInfo = prepareConsumerInfo(consumerConfig);
    Mono<ConsumerInfo> consumerInfoMono = webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(consumerInfo))
            .retrieve()
            .bodyToMono(ConsumerInfo.class);
    Mono<ConsumerInfo> response = consumerInfoMono.single().switchIfEmpty(Mono.error(new RuntimeException("Registration Failed")));
    log.info("Registered " + response.block().toString());

    //Now each consumer will create a DAG on the incoming jobs to decide on what can be executed in parallel
    //Keep polling for jobs and update the DAG
    //Once the Job is done remove from the DAG

    //In order to pick jobs from the DAG or TREE or whatever we can apply various scheduling algorithms
    //We could use simple FCFS or We could use RoundRobin on Tenant Wise Job

    //Have a max job this consumer can pick and based on that pull the job from DB or Broker
    poll();
  }

  private static ConsumerInfo prepareConsumerInfo(ConsumerConfig consumerConfig) {
    ConsumerInfo consumerInfo = new ConsumerInfo();
    String id = "CONN_" + (consumerConfig.getId() != null ? consumerConfig.getId() : DateTime.now().getMillis() + "_" + ManagementFactory.getRuntimeMXBean().getName());
    consumerInfo.setId(id);
    consumerInfo.setName(consumerConfig.getName());
    consumerInfo.setPoll(consumerConfig.getPoll());
    consumerInfo.setTopic(consumerConfig.getTopics().get(0)); //Hard-coding to one topic
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

  @Override
  public void run(String... args) throws Exception {

  }
}
