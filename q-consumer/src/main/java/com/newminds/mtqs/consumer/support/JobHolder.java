package com.newminds.mtqs.consumer.support;

import com.google.common.collect.Maps;
import com.newminds.mtqs.common.job.SimpleJob;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Sunand on 13/09/20
 **/
@Data
@Service
public class JobHolder {

  /**
   * Going to hold all jobs by topic, where topic can be multi-tenant
   * Also try to hold all related jobs within a single consumer. Broker will take care of this.
   */
  private static final Map<String, ConcurrentLinkedDeque<SimpleJob>> SIMPLE_JOB_MAP = Maps.newConcurrentMap();
  /*private static final JobHolder JOB_HOLDER = new JobHolder();

  private JobHolder() {
  }

  @Synchronized
  public JobHolder getInstance() {
    if(JOB_HOLDER != null) {
      return JOB_HOLDER;
    } else {
      return new JobHolder();
    }
  }*/

  public Set<String> getTopics() {
    return SIMPLE_JOB_MAP.keySet();
  }

  public void addJobByTopic(String topic, SimpleJob simpleJob) {
    ConcurrentLinkedDeque<SimpleJob> simpleJobs = SIMPLE_JOB_MAP.containsKey(topic) ? SIMPLE_JOB_MAP.get(topic) : new ConcurrentLinkedDeque<>();
    simpleJobs.add(simpleJob);
    SIMPLE_JOB_MAP.put(topic, simpleJobs);
  }

  //Queue Based FIFO
  public SimpleJob getFirstJob(String topic) {
    ConcurrentLinkedDeque<SimpleJob> simpleJobs = SIMPLE_JOB_MAP.get(topic);
    //Need to add if there is not job return an optional etc
    return simpleJobs.poll();
  }

  //Queue Based FIFO
  public SimpleJob getLastJob(String topic) {
    //Also need to check if TOPIC is even present in this consumer etc
    ConcurrentLinkedDeque<SimpleJob> simpleJobs = SIMPLE_JOB_MAP.get(topic);
    //Need to add if there is not job return an optional etc
    return simpleJobs.getLast();
  }

  public Iterator<SimpleJob> getJobs(String topic) {
    ConcurrentLinkedDeque<SimpleJob> simpleJobs = SIMPLE_JOB_MAP.get(topic);
    return simpleJobs.iterator();
  }

  public void deleteJobs(String topic) {
    SIMPLE_JOB_MAP.remove(topic);
  }

}
