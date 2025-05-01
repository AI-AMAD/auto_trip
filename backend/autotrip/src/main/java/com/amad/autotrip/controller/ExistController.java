package com.amad.autotrip.controller;

import com.amad.autotrip.service.ExistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExistController {

    private final ExistService existService;

    public ExistController(ExistService existService) {
        this.existService = existService;
    }

    @GetMapping("/get/where/info")
    public Boolean getWhereInfo(@RequestParam String username) {

        return existService.existWhereInfo(username);
    }
}
