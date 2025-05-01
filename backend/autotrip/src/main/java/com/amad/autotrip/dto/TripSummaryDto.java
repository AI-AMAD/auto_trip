package com.amad.autotrip.dto;

import lombok.Getter;

@Getter
public class TripSummaryDto {

    private String username;
    private String place;
    private String startYmd;
    private String endYmd;
    private boolean activity;
    private boolean museum;
    private boolean cafe;
    private boolean tourAtt;
}
