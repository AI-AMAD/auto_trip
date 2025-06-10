package com.amad.autotrip.service;

import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.mybatis.HotelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class HotelService {

    private final HotelMapper hotelMapper;

    public HotelService(HotelMapper hotelMapper) {
        this.hotelMapper = hotelMapper;
    }

    public TripScheduleDto getLastPlace(String username) {
        return hotelMapper.getLastPlace(username);
    }
}
