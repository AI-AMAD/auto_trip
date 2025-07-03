package com.amad.autotrip.controller;

import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.service.DiaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/diary/{username}")
    public List<ScheduleDto> getTripDiary(@PathVariable String username) {
        return diaryService.getDiaryByUsername(username);
    }
}
