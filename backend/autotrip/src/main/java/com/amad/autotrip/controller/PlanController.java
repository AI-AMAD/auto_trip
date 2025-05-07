package com.amad.autotrip.controller;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/naver/search")
    public NaverSearchResponseDto naverSearch(@RequestParam String place) {
        return planService.naverSearch(place);
    }


    @GetMapping("/naver/search/image")
    public NaverImageResponseDto naverSearchImage(@RequestParam String place) {
        return planService.naverSearchImage(place);
    }

    @PostMapping("/auto/plan")
    public ResponseEntity<?> autoPlan(@RequestBody TripSummaryDto TripSummaryDto) {
        try {
            // userSetting 정보 배열에 담기


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDto(500, "서버 오류: " + e.getMessage()));
        }
    }
}
