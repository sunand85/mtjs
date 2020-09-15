package com.newminds.mtqs.consumer.service;

import com.newminds.mtqs.common.job.Job;
import com.newminds.mtqs.common.job.JobReport;
import com.newminds.mtqs.common.job.JobStatus;
import com.newminds.mtqs.common.job.SimpleJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 12/09/20
 **/
@Slf4j
@Data
@Service
public abstract class Consumer<I, O> implements Job {

  private String name;
  private SimpleJob jobDetails;
  private JobReport report;

  abstract String getTopic();

  @Override
  public void run() {
    start();
    process();
    setStatus(JobStatus.STOPPING);
    //Store the job report in DB
    log.info("==================");
  }

  private void start() {
    log.info("Thread Name, {}", Thread.currentThread().getName());
    report = new JobReport();
    try {
      name = jobDetails.getName();
    } catch (Exception e) {
      log.error("FASAAK - {}", e.getMessage(), e);
    }
  }

  private void setStatus(JobStatus status) {
    if(isInterrupted()) {
      log.info("Job '{}' has been interrupted, aborting execution.", name);
    }
    log.info("Job '{}' {}", name, status.name().toLowerCase());
    report.setStatus(status);
  }

  private boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
  }

  abstract void process();
}
