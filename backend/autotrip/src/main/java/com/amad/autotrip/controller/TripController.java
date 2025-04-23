package com.amad.autotrip.controller;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.service.TripService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/save/place")
    public void savePlace(@RequestBody PlaceDto placeDto) {
        tripService.savePlace(placeDto);
    }
}
