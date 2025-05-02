package com.amad.autotrip.controller;

import com.amad.autotrip.dto.NaverSearchResponseDto;
import com.amad.autotrip.service.PlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
