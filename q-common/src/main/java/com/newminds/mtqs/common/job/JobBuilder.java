package com.newminds.mtqs.common.job;

/**
 * Created by Sunand on 04/09/20
 **/
public class JobBuilder {

  private String name;
  private Header header;
  private Object payload;
  private JobType type = JobType.SIMPLE;
  private JobParameters parameters;

  private JobBuilder() {
  }

  public static JobBuilder asNewJob() {
    return new JobBuilder();
  }

  public JobBuilder name(String name) {
    this.name = name;
    return this;
  }

  public JobBuilder header(Header header) {
    this.header = header;
    return this;
  }

  public JobBuilder payload(Object payload) {
    this.payload = payload;
    return this;
  }

  public JobBuilder type(JobType type) {
    this.type = type;
    return this;
  }

  public JobBuilder parameters(JobParameters parameters) {
    this.parameters = parameters;
    return this;
  }

  public Job build() {
    Job job = null;
    switch (type) {
      case SIMPLE:
        job = new SimpleJob(name, header, payload, parameters);
        break;
//        return job;
    }

    return job;
  }
}

