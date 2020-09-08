package com.newminds.mtqs.common.job;

import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created by Sunand on 04/09/20
 **/
@Data
@Builder
public class Header {
    private String traceId;
    private String userId;
    private String tenantId;
    private String tenantType;
    private String topic;
    private String source;
    private DateTime now;
    private Map<String, Object> properties;
}
