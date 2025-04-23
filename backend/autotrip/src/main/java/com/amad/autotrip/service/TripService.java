package com.amad.autotrip.service;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.mybatis.TripMapper;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private TripMapper tripMapper;

    public TripService(TripMapper tripMapper) {
        this.tripMapper = tripMapper;
    }

    public void savePlace(PlaceDto placeDto) {
        PlaceDto placeInfo = new PlaceDto();
        placeInfo.setUsername(placeDto.getUsername());
        placeInfo.setPlace(placeDto.getPlace());

        tripMapper.savePlace(placeInfo);
    }

}
