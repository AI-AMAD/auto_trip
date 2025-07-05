package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SearchPlaceDto {
    private String formattedAddress;
    private DisplayNameDto displayName;
}
