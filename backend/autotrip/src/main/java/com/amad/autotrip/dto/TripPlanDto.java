package com.amad.autotrip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripPlanDto {
    private Long tripId;
    private String username;
    private String place;
    private String startYmd;
    private String endYmd;
    private String settings;
}
