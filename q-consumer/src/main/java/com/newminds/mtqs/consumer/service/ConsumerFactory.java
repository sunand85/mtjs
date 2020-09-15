package com.newminds.mtqs.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sunand on 16/09/20
 **/
@Service
public class ConsumerFactory{

  private final Map<String, Consumer> consumerByTopic;

  @Autowired
  public ConsumerFactory(List<Consumer> consumers) {
    consumerByTopic = consumers.stream().collect(Collectors.toMap(Consumer::getTopic, consumer -> consumer));
  }

  public Consumer getConsumerByTopic(String topic) {
    return consumerByTopic.get(topic);
  }
}
