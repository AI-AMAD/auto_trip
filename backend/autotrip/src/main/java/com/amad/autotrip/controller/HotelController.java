package com.amad.autotrip.controller;


import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.service.HotelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/get/lastplace")
    public TripScheduleDto getLastPlace(@RequestParam String username) {
        return hotelService.getLastPlace(username);
    }
}
