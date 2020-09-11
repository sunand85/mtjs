package com.newminds.mtqs.broker.selectors;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import com.newminds.mtqs.common.topic.Topic;

/**
 * Created by Sunand on 12/09/20
 **/
public interface Selector {

  ConsumerInfo select(Topic topic);
}
