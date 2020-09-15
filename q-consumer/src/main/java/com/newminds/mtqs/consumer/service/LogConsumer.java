package com.newminds.mtqs.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 16/09/20
 **/
@Slf4j
@Service
public class LogConsumer extends Consumer {

  @Override
  String getTopic() {
    return "log";
  }

  @Override
  void process() {
    log.info("I am just going to log whatever is thrown at me");
    log.info("DATA START");
    log.info("Job Details, {}", getJobDetails());
    log.info("DATA END");
  }
}
