package com.newminds.mtqs.broker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * Created by Sunand on 10/09/20
 **/
public class ConsumerInfoCustomImpl implements ConsumerInfoCustom {
  @Autowired
  private ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public ReactiveMongoTemplate getReactiveMongoTemplate() {
    return reactiveMongoTemplate;
  }
}
