package com.newminds.mtqs.common.job;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Sunand on 27/08/2020.
 **/
@Data
public class JobParameters implements Serializable {
  private static final long serialVersionUID = -1397246753455237004L;

  public static final String DEFAULT_JOB_NAME = "job";
  public static final long DEFAULT_ERROR_THRESHOLD = Long.MAX_VALUE;
  public static final int DEFAULT_BATCH_SIZE = 100;

  private long errorThreshold;
  private boolean jmxMonitoring;
  private int batchSize;
  private boolean batchScanningEnabled;

  public JobParameters() {
    this.errorThreshold = DEFAULT_ERROR_THRESHOLD;
    this.batchSize = DEFAULT_BATCH_SIZE;
  }
}
