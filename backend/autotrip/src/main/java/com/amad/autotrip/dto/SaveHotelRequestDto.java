package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaveHotelRequestDto {
    private String username;
    private String date;
    private String name;
    private String address;
    private String imageUrl;
}
