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
    public Mono<ResponseEntity<Object>> autoPlan(@RequestBody TripSummaryDto tripSummaryDto) {
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

            // setting에 맞는 여행 장소 검색 및 이미지 가져오기
            return planService.naverSearch(place, settings)
                    .flatMap(results -> Mono.just(ResponseEntity.ok((Object) results))) // 성공 시: List<PlaceWithImage>를 Object로 캐스팅
                    .onErrorResume(e -> {
                        Map<String, String> errorResponse = new HashMap<>();
                        errorResponse.put("error", "서버 오류");
                        errorResponse.put("message", e.getMessage());
                        return Mono.just(ResponseEntity.badRequest().body((Object) errorResponse)); // 에러 시: Map을 Object로 캐스팅
                    });
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "요청 처리 오류");
            errorResponse.put("message", e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body((Object) errorResponse));
        }
    }
}
