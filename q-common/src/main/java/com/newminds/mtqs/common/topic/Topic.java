package com.newminds.mtqs.common.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 04/09/20
 **/
@Data
@Builder
@Document("topic")
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
  @Id
  private String id;
  @Indexed(background = true, unique = true)
  private String name;
  private boolean multiTenant;
  private boolean durable; //By Default the Jobs against a topic will be deleted. If it is set to true we will copy it to another durable storage.
  private boolean parallel;
  private boolean ordered;

  private String offset; // Last job read by consumer from this topic

  @Transient
  private String tenantId;
  //Have this topic send to a particular consumer of choice then store the consumer id here
}
