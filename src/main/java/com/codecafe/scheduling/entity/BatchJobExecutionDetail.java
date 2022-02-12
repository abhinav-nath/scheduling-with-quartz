package com.codecafe.scheduling.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BatchJobExecutionDetail {

    private int jobExecutionId;
    private Date startTime;
    private Date endTime;
    private String status;

}