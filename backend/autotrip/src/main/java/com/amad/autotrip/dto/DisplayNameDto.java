package com.amad.autotrip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DisplayNameDto {
    private String text;
    private String languageCode;
}
