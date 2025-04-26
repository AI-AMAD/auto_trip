package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.PlaceDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WhereMapper {

    PlaceDto getPlaceByUsername(String username);

    int savePlace(PlaceDto placeDto);
}
