package com.newminds.mtqs.common.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newminds.mtqs.common.job.Stats;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by Sunand on 07/09/20
 **/
@Data
@Document("consumer_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerInfo implements Serializable {
  private static final long serialVersionUID = 1963326622871524786L;

  @Id
  private String id;
  private String name;
  private String host;
  private String port;
  private String startTime;
  private Stats stats;

  private long poll = 1000; //in ms
  private String topic; //It can be a list as well in future
}
