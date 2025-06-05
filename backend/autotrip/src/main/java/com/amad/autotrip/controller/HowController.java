package com.amad.autotrip.controller;

import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.dto.TripScheduleDto;
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

    @GetMapping("/schedule/{username}/{tripId}")
    public List<TripScheduleDto> getTripScheduleByTripId(@PathVariable String username, @PathVariable Long tripId) {
        return howService.getTripScheduleByTripId(username, tripId);
    }

    @DeleteMapping("/schedule/{username}/{tripId}")
    public ResponseEntity<Void> deleteSchedules(@PathVariable String username, @PathVariable Long tripId, @RequestBody List<Long> scheduleIds) {
        log.info("delete schedules by username -------> {}", username);
        log.info("delete schedules by tripId --------> {}", tripId);
        log.info("delete schedules by scheduleIds -----------> {}", scheduleIds);
        howService.deleteSchedules(username, tripId, scheduleIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule/{username}")
    public ResponseEntity<Void> createSchedules(@PathVariable String username, @RequestBody List<TripScheduleDto> schedules) {
        howService.createSchedules(username, schedules);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/schedule/{username}")
    public ResponseEntity<Void> updateSchedules(@PathVariable String username, @RequestBody List<TripScheduleDto> schedules) {
        log.info("update schedules by username -------> {}", username);
        log.info("update schedules by schedules --------> {}", schedules);
        log.info("updateSchedules 는 어떻게 호출된거지??");
        howService.updateSchedules(username, schedules);
        return ResponseEntity.ok().build();
    }
}
