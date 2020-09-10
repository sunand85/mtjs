package com.newminds.mtqs.common.job;

import java.util.concurrent.Callable;

/**
 * Created by Sunand on 27/08/2020.
 **/
public interface Job extends Callable<JobReport> {
  String getId();

  JobType getJobType();
}
