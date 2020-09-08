package com.newminds.mtqs.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by Sunand on 08/09/20
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerConfig {
  private String id;
  private String name;
  private List<String> topics;
  private int poll = 2000;
  private String brokerUrl;
}
