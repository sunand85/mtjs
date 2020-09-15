package com.newminds.mtqs.consumer.scheduler;

import com.newminds.mtqs.common.job.JobStatus;
import com.newminds.mtqs.common.job.SimpleJob;
import com.newminds.mtqs.consumer.service.Consumer;
import com.newminds.mtqs.consumer.service.ConsumerFactory;
import com.newminds.mtqs.consumer.support.JobHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Sunand on 13/09/20
 **/
@Slf4j
@Service
public class MultiTenantRoundRobinScheduler implements Scheduler {

  private final JobHolder jobHolder;
  private ConsumerFactory consumerFactory;

  private final Executor executor = Executors.newFixedThreadPool(10);

  public MultiTenantRoundRobinScheduler(JobHolder jobHolder, ConsumerFactory consumerFactory) {
    this.jobHolder = jobHolder;
    this.consumerFactory = consumerFactory;
  }

  @Override
  public void schedule() {
    //This just got more complicated since we have a fixed thread pool which needs to be used wisely
    //.filter(s -> jobHolder.getFirstJob(s) == null)
      jobHolder.getTopics().stream().filter(topic -> jobHolder.isTopicPresent(topic)).forEach(topic -> {
        //Need to invoke the jobs here. This could be a Java Future or Callable and proceed calling the next job
//        Consumer consumer = consumerFactory.getConsumerByTopic(topic);
//        consumer.setJobDetails(jobHolder.getFirstJob(topic));
        CompletableFuture.runAsync(executableConsumer(topic), executor);
      });
  }

  private Consumer executableConsumer(String topic) {
    Consumer consumer = consumerFactory.getConsumerByTopic(topic);

    SimpleJob job = jobHolder.getFirstJob(topic);
//    log.info("Going to Execute - {}", job.getName());
    if(job == null) {
      Consumer waiting = consumerFactory.getConsumerByTopic("WAITING");
      waiting.setJobDetails(new SimpleJob("WAIT", null, null, null));
      return waiting;
    }
    consumer.setJobDetails(job);
    return consumer;
  }

  private void runJob(String topic) {
    SimpleJob job = jobHolder.getFirstJob(topic);

    if(JobStatus.ACCEPTED == job.getStatus()) {
      job.setStatus(JobStatus.STARTING);
      // Mark the job as running for this topic per tenant.
      // So that next job is read from this topic if the previous one is running it has to skip and proceed to next.
      log.info("Job Id, {}", job.getId());
      log.info("Header, {}", job.getHeader());
      log.info("Payload, {}", job.getPayload());
      //In case of any exception just set the status to FAILED and move on for now.
      job.setStatus(JobStatus.COMPLETED);
    }
    //Or just skip it
  }
}
