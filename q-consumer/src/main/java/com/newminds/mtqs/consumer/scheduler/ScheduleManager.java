package com.newminds.mtqs.consumer.scheduler;

import com.newminds.mtqs.consumer.support.JobHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Sunand on 13/09/20
 **/
@Service
public class ScheduleManager {

  private final MultiTenantRoundRobinScheduler roundRobinScheduler;
  private JobHolder jobHolder;

  public ScheduleManager(MultiTenantRoundRobinScheduler roundRobinScheduler, JobHolder jobHolder) {
    this.roundRobinScheduler = roundRobinScheduler;
    this.jobHolder = jobHolder;
  }

  @Scheduled(fixedRate = 1000)
  public void poll() {
    //Skip Schedule if the jobs are yet to complete and its double the number of fixed threads.
    roundRobinScheduler.schedule();
  }
}
