package com.amad.autotrip.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TextSearchResponseDto {
    private List<SearchPlaceDto> places;
}

