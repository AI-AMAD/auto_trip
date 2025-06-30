package com.amad.autotrip.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDto {
    private String name;
    private String address;
    private String imageUrl;
}
