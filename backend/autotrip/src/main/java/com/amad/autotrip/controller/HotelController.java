package com.amad.autotrip.controller;


import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @PostMapping("/get/nearby-hotels")
    public Mono<ResponseEntity> searchHotels(@RequestBody) {

    }
}
