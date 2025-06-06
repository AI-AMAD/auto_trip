package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDto {
    private Long scheduleId;
    private Integer activityOrder;
    private String activityType;
    private String activityName;
    private String activityAddress;
    private String activityImageUrl;
}
