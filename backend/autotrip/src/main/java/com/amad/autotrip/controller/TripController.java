package com.amad.autotrip.controller;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.service.TripService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> savePlace(@RequestBody PlaceDto placeDto) {
        try {
            boolean isUpdated = tripService.savePlace(placeDto);
            if (isUpdated) {
                return ResponseEntity.ok("Existing place updated successfully");
            }
            return ResponseEntity.ok("Place saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save place: " + e.getMessage());
        }
    }
}
