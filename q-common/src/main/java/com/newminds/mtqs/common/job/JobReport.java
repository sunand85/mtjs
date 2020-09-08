package com.newminds.mtqs.common.job;

import lombok.Data;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Sunand on 27/08/2020.
 **/
@Data
public class JobReport implements Serializable {
  private static final long serialVersionUID = -3652012987887540826L;

  private String jobName;
  private JobParameters parameters;
  private JobMetrics metrics;
  private JobStatus status;
  private Throwable lastError;
  private Properties systemProperties;
}
