package com.amad.autotrip.controller;

import com.amad.autotrip.dto.Users;
import com.amad.autotrip.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/join")
    public String joinProcess(@RequestBody Users user) {

        Boolean joinSuccess;
        joinSuccess = userService.join(user);
        if (joinSuccess) {
            return "회원 가입 완료!";
        } else {
            return "이미 가입되어있습니다.";
        }
    }
}
