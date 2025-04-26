package com.amad.autotrip.service;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.mybatis.WhereMapper;
import org.springframework.stereotype.Service;

@Service
public class WhereService {

    private final WhereMapper whereMapper;

    public WhereService(WhereMapper whereMapper) {
        this.whereMapper = whereMapper;
    }

    public PlaceDto getPlaceByUsername(String username) {
        return whereMapper.getPlaceByUsername(username);
    }

    public boolean savePlace(PlaceDto placeDto) {
        // UPSERT 실행, 업데이트 여부 반환
        int result = whereMapper.savePlace(placeDto);
        return result > 1; // MySQL의 ON DUPLICATE KEY UPDATE는 업데이트 시 2 반환
    }

}
