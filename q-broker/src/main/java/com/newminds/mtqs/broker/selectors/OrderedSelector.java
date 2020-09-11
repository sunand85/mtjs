package com.newminds.mtqs.broker.selectors;

import com.newminds.mtqs.broker.repository.ConsumerInfoRepository;
import com.newminds.mtqs.broker.repository.TopicConsumerRepository;
import com.newminds.mtqs.broker.repository.TopicRepository;
import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.topic.Topic;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 12/09/20
 **/
@Service
public class OrderedSelector extends AbstractSelector {

  public OrderedSelector(ConsumerInfoRepository consumerInfoRepository, TopicRepository topicRepository, TopicConsumerRepository topicConsumerRepository) {
    super(consumerInfoRepository, topicRepository, topicConsumerRepository);
  }

  @Override
  public ConsumerInfo select(Topic topic) {
    return null;
  }
}
