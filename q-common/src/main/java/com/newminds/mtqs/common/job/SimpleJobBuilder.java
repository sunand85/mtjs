package com.newminds.mtqs.common.job;

/**
 * Created by Sunand on 04/09/20
 **/
public class SimpleJobBuilder {

  private String name;
  private Header header;
  private Object payload;
  private JobType type = JobType.SIMPLE;
  private JobParameters parameters;

  private SimpleJobBuilder() {
  }

  public static SimpleJobBuilder asNewJob() {
    return new SimpleJobBuilder();
  }

  public SimpleJobBuilder name(String name) {
    this.name = name;
    return this;
  }

  public SimpleJobBuilder header(Header header) {
    this.header = header;
    return this;
  }

  public SimpleJobBuilder payload(Object payload) {
    this.payload = payload;
    return this;
  }

  public SimpleJobBuilder type(JobType type) {
    this.type = type;
    return this;
  }

  public SimpleJobBuilder parameters(JobParameters parameters) {
    this.parameters = parameters;
    return this;
  }

  public SimpleJob build() {
    return new SimpleJob(name, header, payload, parameters);
  }
}

