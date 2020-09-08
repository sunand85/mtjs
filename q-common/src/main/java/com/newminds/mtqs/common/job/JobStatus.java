package com.newminds.mtqs.common.job;

/**
 * Created by Sunand on 27/08/2020.
 **/
public enum JobStatus {
  /**
   * The job is starting (opening record reader/writer).
   */
  STARTING,

  /**
   * The job has started.
   */
  STARTED,

  /**
   * The job is stopping (closing record reader/writer)
   */
  STOPPING,

  /**
   * The job has failed (due to read/write errors or errorThreshold exceeded).
   */
  FAILED,

  /**
   * The job has completed normally without any exception.
   */
  COMPLETED,

  /**
   * The job has been interrupted.
   */
  ABORTED
}
