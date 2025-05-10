package com.amad.autotrip.dto;

import lombok.Getter;

@Getter
public class PlaceWithImage {
    private final NaverSearchResponseDto.Item place;
    private final String imageUrl;

    public PlaceWithImage(NaverSearchResponseDto.Item place, String imageUrl) {
        this.place = place;
        this.imageUrl = imageUrl;
    }
}
