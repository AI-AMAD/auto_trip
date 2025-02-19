package com.amad.autotrip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auto")
public class MainController {

  @GetMapping("/maintest")
  public String mainP() {
    return "Main Controller";
  }

  
}