package com.amad.autotrip.oauth2;

import com.amad.autotrip.dto.KakaoUserInfoResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;

    public KakaoOauthController(KakaoOauthService kakaoOauthService) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoOauthService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoOauthService.getUserInfo(accessToken);

        // 여기에 서버 사용자 로그인(인증) 또는 회원가입 로직 추가
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
