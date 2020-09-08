package com.newminds.mtqs.common.job;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 27/08/2020.
 **/
@Data
@Document("simple_job")
public class SimpleJob implements Job {

  @Id
  private String id;
  private String name;
  private Header header;
  private Object payload;
  private final JobParameters parameters;

  public SimpleJob(String name, Header header, Object payload, JobParameters parameters) {
    this.name = name;
    this.header = header;
    this.payload = payload;
    this.parameters = parameters;
  }

  @Override
  public JobReport call() throws Exception {
    return null;
  }
}
