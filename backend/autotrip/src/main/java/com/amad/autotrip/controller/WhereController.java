package com.amad.autotrip.controller;

import com.amad.autotrip.dto.PlaceDto;
import com.amad.autotrip.service.WhereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WhereController {

    private final WhereService whereService;

    public WhereController(WhereService whereService) {
        this.whereService = whereService;
    }

    @GetMapping("/place")
    public ResponseEntity<PlaceDto> getPlace(@RequestParam String username) {
        PlaceDto placeDto = whereService.getPlaceByUsername(username);
        if (placeDto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(placeDto);
    }

    @PostMapping("/save/place")
    public ResponseEntity<String> savePlace(@RequestBody PlaceDto placeDto) {
        try {
            boolean isUpdated = whereService.savePlace(placeDto);
            if (isUpdated) {
                return ResponseEntity.ok("Existing place updated successfully");
            }
            return ResponseEntity.ok("Place saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save place: " + e.getMessage());
        }
    }
}
