package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.SaveHotelRequestDto;
import com.amad.autotrip.dto.TripScheduleDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper {

    TripScheduleDto getLastPlace(String username);

    void upsertAccommodation(SaveHotelRequestDto request);
}
