package com.newminds.mtqs.common.job;

import com.newminds.mtqs.common.consumer.ConsumerInfo;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by Sunand on 09/09/20
 **/
@Data
public class TopicConsumerLookup {
  private String topicId;
  @Indexed(background = true)
  private String topicName;
  @Indexed(background = true)
  private String consumerId;
  @Indexed(background = true)
  private String tenantId;

  @Transient
  private ConsumerInfo consumerInfo;
}
