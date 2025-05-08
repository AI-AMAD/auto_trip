package com.amad.autotrip.controller;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<?> autoPlan(@RequestBody TripSummaryDto tripSummaryDto) {
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
                settings.add("관광 명소");
            }

            // setting에 맞는 여행 장소 검색
//            planService.naverSearch(place, settings);

            // 검색한 여행 장소 진위 확인

            // 여행 장소 이미지 검색

            // DB 저장


            return ResponseEntity.ok("여행 계획 수립 완료!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDto(500, "서버 오류: " + e.getMessage()));
        }
    }
}
