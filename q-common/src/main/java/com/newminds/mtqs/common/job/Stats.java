package com.newminds.mtqs.common.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Sunand on 04/09/20
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {
  private int activeJobs;
  private int processingRate;
  private int successCount;
  private int failureCount;
  private int cpu;
  private long memory;
  private long freeDisk;
}
