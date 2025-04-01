package com.amad.autotrip.oauth2;

import com.amad.autotrip.dto.KakaoUserInfoResponseDto;
import com.amad.autotrip.dto.LoginDto;
import com.amad.autotrip.dto.TokenDto;
import com.amad.autotrip.dto.UserDto;
import com.amad.autotrip.repository.UserRepository;
import com.amad.autotrip.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/api")
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;
    private final UserRepository userRepository;
    private final UserService userService;

    public KakaoOauthController(KakaoOauthService kakaoOauthService, UserRepository userRepository, UserService userService) {
        this.kakaoOauthService = kakaoOauthService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/auth/kakao/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

        String accessToken = kakaoOauthService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoOauthService.getUserInfo(accessToken);

        UserDto user = new UserDto();
        user.setUsername(String.valueOf(userInfo.getId()));
        user.setPassword("kakao");
        user.setNickname(userInfo.getKakaoAccount().getProfile().getNickName());

        ResponseEntity<TokenDto> token = null;

        if (userRepository.findOneWithAuthoritiesByUsername(user.getUsername()).orElse(null) != null) {
            LoginDto loginDto = new LoginDto();
            loginDto.setUsername(String.valueOf(userInfo.getId()));
            loginDto.setPassword("kakao");

            token = userService.login(loginDto);
        } else {
            userService.signup(user);
            LoginDto loginDto = new LoginDto();
            loginDto.setUsername(String.valueOf(userInfo.getId()));
            loginDto.setPassword("kakao");

            token = userService.login(loginDto);
        }

        log.info("token----->: " + token);
        String redirectUrl = "http://localhost:5173/?token=" + token.getBody().getToken();
        response.sendRedirect(redirectUrl);

    }

}
