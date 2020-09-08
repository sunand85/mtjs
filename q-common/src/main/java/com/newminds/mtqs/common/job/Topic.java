package com.newminds.mtqs.common.job;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 04/09/20
 **/
@Data
@Builder
@Document("topic")
public class Topic {
  @Id
  private String id;
  private String name;
  private String tenantId;
  private boolean durable; //By Default the Jobs against a topic will be deleted. If it is set to true we will copy it to another durable storage.
  private boolean parallel;
  private boolean ordered;
  private int consumerCount = 1;
}
