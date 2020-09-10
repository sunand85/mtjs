package com.newminds.mtqs.common.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Sunand on 09/09/20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
  private String jobId;
  private String statusId;
  private JobStatus jobStatus;
}
