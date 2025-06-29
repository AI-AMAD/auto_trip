package com.amad.autotrip.controller;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.service.HowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class HowController {

    private final HowService howService;

    public HowController(HowService howService) {
        this.howService = howService;
    }

    @GetMapping("/schedule/{username}")
    public List<ScheduleDto> getTripSchedule(@PathVariable String username) {
        return howService.getTripPlanByUsername(username);
    }

    @DeleteMapping("/schedule/{username}/{tripId}")
    public ResponseEntity<String> deleteSchedules(@PathVariable String username, @PathVariable Long tripId) {
        try {
            howService.deleteSchedules(username, tripId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("스케줄 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.badRequest().body("스케줄 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/schedule/{username}/{tripId}")
    public ResponseEntity<String> createSchedules(@PathVariable String username, @PathVariable Long tripId, @RequestBody List<TripScheduleDto> schedules) {
        try {
            howService.createSchedules(username, schedules); // 스케줄 생성 시도
            howService.updateFinalYn(tripId); // 성공 시 final_yn 업데이트
            return ResponseEntity.ok("스케줄이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            log.error("스케줄 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.badRequest().body("스케줄 생성에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/refresh/trip-place")
    public ResponseEntity<List<PlaceWithImage>> refreshTripPlace(@RequestBody RefreshRequestDto refreshRequestDto) {
        try {
            String place = refreshRequestDto.getPlace();
            String activityType = refreshRequestDto.getActivityType();
            List<ExcludedPlaceDto> excludedPlaces = refreshRequestDto.getExcludedPlaces();

            log.info("Received place: {}, activityType: {}", place, activityType);  // 디버깅용 로그

            List<PlaceWithImage> newPlaces = howService.searchNewPlaces(place, activityType, excludedPlaces);
            return ResponseEntity.ok(newPlaces);
        } catch (Exception e) {
            log.error("여행 장소 새로고침 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

}
