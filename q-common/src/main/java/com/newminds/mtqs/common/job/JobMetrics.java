package com.newminds.mtqs.common.job;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunand on 27/08/2020.
 **/
@Data
public class JobMetrics implements Serializable {

  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private long readCount;
  private long writeCount;
  private long filterCount;
  private long errorCount;
  private Map<String, Object> customMetrics = new HashMap<>();
}
