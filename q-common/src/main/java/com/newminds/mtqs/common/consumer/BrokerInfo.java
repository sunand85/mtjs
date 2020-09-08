package com.newminds.mtqs.common.consumer;

import lombok.Data;

/**
 * Created by Sunand on 04/09/20
 **/
@Data
public class BrokerInfo {
  private String host;
  private String port;
  private int retries = 5;
  private boolean autoReconnect = true;
}
