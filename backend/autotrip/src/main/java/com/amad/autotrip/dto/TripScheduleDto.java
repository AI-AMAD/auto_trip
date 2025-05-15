package com.amad.autotrip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripScheduleDto {
    private Long scheduleId;
    private Long tripId;
    private String startYmd = "";
    private String endYmd = "";
    private int activityOrder;
    private String activityType;
    private String activityName;
    private String activityAddress;
    private String activityImageUrl;
}
