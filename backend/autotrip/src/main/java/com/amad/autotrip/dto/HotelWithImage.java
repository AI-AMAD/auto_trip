package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HotelWithImage {
    private String name;
    private String address;
    private String imageUrl;
}
