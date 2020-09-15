package com.newminds.mtqs.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 16/09/20
 **/
@Slf4j
@Service
public class WaitingConsumer extends Consumer {
  @Override
  String getTopic() {
    return "WAITING";
  }

  @Override
  void process() {
    log.info("Waiting for Jobs to Appear");
  }
}
