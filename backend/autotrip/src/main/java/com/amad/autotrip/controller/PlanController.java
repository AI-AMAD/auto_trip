package com.amad.autotrip.controller;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/auto/plan")
    public Mono<ResponseEntity<Map<String, Object>>> autoPlan(@RequestBody TripSummaryDto tripSummaryDto) {
        try {
            // userSetting 정보 배열에 담기
            List<String> settings = new ArrayList<>();

            String username = tripSummaryDto.getUsername();
            String place = tripSummaryDto.getPlace();

            // Boolean 값을 체크하여 true인 경우 리스트에 추가
            if (tripSummaryDto.isActivity()) {
                settings.add("레포츠");
            }
            if (tripSummaryDto.isMuseum()) {
                settings.add("박물관");
            }
            if (tripSummaryDto.isCafe()) {
                settings.add("카페");
            }
            if (tripSummaryDto.isTourAtt()) {
                settings.add("관광");
            }

            // 맛집은 무조건 추가
            settings.add("맛집");

            // setting에 맞는 여행 장소 검색 및 이미지 가져오기
            return planService.naverSearch(username, place, settings)
                    .flatMap(places -> planService.createAndSaveTripPlan(tripSummaryDto, places, settings)
                            .flatMap(tripId -> {
                                Map<String, Object> successResponse = new HashMap<>();
                                successResponse.put("tripId", tripId);
                                return Mono.just(ResponseEntity.ok(successResponse));
                            }))
                    .onErrorResume(e -> {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("error", "서버 오류");
                        errorResponse.put("message", e.getMessage());
                        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                    });
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "요청 처리 오류");
            errorResponse.put("message", e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }
    }
}
