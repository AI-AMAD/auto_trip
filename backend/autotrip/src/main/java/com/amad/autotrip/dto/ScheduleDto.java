package com.amad.autotrip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 값은 JSON에서 제외
public class ScheduleDto {
    private Long tripId;
    private String username;
    private String place;
    private String settings;
    private Map<String, List<ActivityDto>> startYmd;
    private Map<String, List<ActivityDto>> endYmd;
    private transient List<ActivityWithDateDto> activities; // JSON 직렬화에서 제외
}
