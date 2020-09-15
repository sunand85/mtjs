package com.newminds.mtqs.common.job;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 27/08/2020.
 **/
@Data
@Document("simple_job")
@NoArgsConstructor
@AllArgsConstructor
public class SimpleJob {

  @Id
  private String id;
  private String name;
  private Header header;
  private String topic;
  @Setter(value = AccessLevel.PRIVATE)
  private JobType jobType = JobType.SIMPLE;
  private Object payload;
  private JobParameters parameters;
  private JobStatus status;

  public SimpleJob(String name, Header header, Object payload, JobParameters parameters) {
    this.name = name;
    this.header = header;
    this.payload = payload;
    this.parameters = parameters;
  }
}
