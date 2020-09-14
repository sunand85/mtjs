package com.newminds.mtqs.consumer.scheduler;

import com.newminds.mtqs.common.job.JobStatus;
import com.newminds.mtqs.common.job.SimpleJob;
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

  private final Executor executor = Executors.newFixedThreadPool(10);

  public MultiTenantRoundRobinScheduler(JobHolder jobHolder) {
    this.jobHolder = jobHolder;
  }

  @Override
  public void schedule() {
    //This just got more complicated since we have a fixed thread pool which needs to be used wisely
      jobHolder.getTopics().stream().forEach(topic -> {
        //Need to invoke the jobs here. This could be a Java Future or Callable and proceed calling the next job
        CompletableFuture.runAsync(() -> runJob(topic), executor);
      });
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
