package com.newminds.mtqs.broker.selectors;

import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.TopicConsumerRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 12/09/20
 **/
@Service
public abstract class AbstractSelector implements Selector {

  protected ConsumerInfoRepository consumerInfoRepository;
  protected TopicRepository topicRepository;
  protected TopicConsumerRepository topicConsumerRepository;

  @Autowired
  public AbstractSelector(ConsumerInfoRepository consumerInfoRepository, TopicRepository topicRepository, TopicConsumerRepository topicConsumerRepository) {
    this.consumerInfoRepository = consumerInfoRepository;
    this.topicRepository = topicRepository;
    this.topicConsumerRepository = topicConsumerRepository;
  }
}
