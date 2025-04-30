package com.amad.autotrip.controller;

import com.amad.autotrip.dto.SettingDto;
import com.amad.autotrip.dto.TripSummaryDto;
import com.amad.autotrip.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping("/save/setting")
    public ResponseEntity<String> saveSetting(@RequestBody SettingDto settingDto) {
        try {
            settingService.saveSetting(settingDto);
            return ResponseEntity.ok("설정이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("설정 저장 실패: " + e.getMessage());
        }
    }

    @GetMapping("/get/setting")
    public ResponseEntity<TripSummaryDto> getSetting(@RequestParam String username) {
        try {
            TripSummaryDto tripSummary = settingService.getTripSummary(username);
            if (tripSummary == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tripSummary);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
