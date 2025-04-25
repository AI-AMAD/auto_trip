package com.amad.autotrip.service;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.mybatis.TripMapper;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final TripMapper tripMapper;

    public TripService(TripMapper tripMapper) {
        this.tripMapper = tripMapper;
    }

    public boolean savePlace(PlaceDto placeDto) {
        // UPSERT 실행, 업데이트 여부 반환
        int result = tripMapper.savePlace(placeDto);
        return result > 1; // MySQL의 ON DUPLICATE KEY UPDATE는 업데이트 시 2 반환
    }

}
