package com.amad.autotrip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingDto {

    private String username;
    private String startYmd;
    private String endYmd;
    private boolean activity;
    private boolean museum;
    private boolean cafe;
    private boolean tourAtt;
}
