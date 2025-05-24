package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityWithDateDto {
    private String date; // start_ymd 또는 end_ymd
    private Integer activityOrder;
    private String activityType;
    private String activityName;
    private String activityAddress;
    private String activityImageUrl;
}
