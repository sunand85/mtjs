package com.newminds.mtqs.consumer.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Sunand on 08/09/20
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerConfig {
  private String id;
  private String name;
  private String host;
  private String port;
  private String brokerUrl;
}
