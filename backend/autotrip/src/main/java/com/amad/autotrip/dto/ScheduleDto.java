package com.amad.autotrip.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private Long tripId;
    private String username;
    private String place;
    private String settings;
    private Map<String, List<ActivityDto>> startYmd;
    private Map<String, List<ActivityDto>> endYmd;
    // MyBatis에서 임시로 List를 받기 위한 필드
    private List<ActivityWithDateDto> activities;
}
