package com.newminds.mtqs.common.listener;

import com.newminds.mtqs.common.job.JobParameters;
import com.newminds.mtqs.common.job.JobReport;

/**
 * Created by Sunand on 27/08/2020.
 **/
public interface JobListener {

  /**
   * Called before starting the job.
   *
   * @param jobParameters the job parameters
   */
  default void beforeJob(final JobParameters jobParameters) {
    // no-op
  }

  /**
   * Called after the job is finished (successfully or with a failure).
   *
   * @param jobReport The job execution report
   */
  default void afterJob(final JobReport jobReport) {
    // no-op
  }
}
