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
   
    @DeleteMapping("/schedule/{username}/{tripId}")
    public ResponseEntity<Void> deleteSchedules(@PathVariable String username, @PathVariable Long tripId) {
        howService.deleteSchedules(username, tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule/{username}")
    public ResponseEntity<Void> createSchedules(@PathVariable String username, @RequestBody List<TripScheduleDto> schedules) {
        howService.createSchedules(username, schedules);
        return ResponseEntity.ok().build();
    }
}
