package com.amad.autotrip.controller;


import com.amad.autotrip.dto.HotelWithImage;
import com.amad.autotrip.dto.SaveHotelRequestDto;
import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @GetMapping("/search-hotels")
    public Mono<ResponseEntity<List<HotelWithImage>>> searchHotels(@RequestParam String query) {
        return hotelService.getHotelsWithImages(query)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @PostMapping("/save-hotel")
    public ResponseEntity<String> saveHotel(@RequestBody SaveHotelRequestDto request) {
        try {
            hotelService.saveHotel(request);
            return ResponseEntity.ok("호텔이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("호텔 저장 실패: " + e.getMessage());
        }
    }
}
