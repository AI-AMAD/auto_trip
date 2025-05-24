package com.amad.autotrip.controller;

import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.dto.TripPlanDto;
import com.amad.autotrip.service.HowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
