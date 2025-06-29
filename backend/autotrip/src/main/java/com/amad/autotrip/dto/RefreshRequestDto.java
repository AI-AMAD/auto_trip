package com.amad.autotrip.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshRequestDto {
    private String place;
    private String activityType;
    private List<ExcludedPlaceDto> excludedPlaces;
}
