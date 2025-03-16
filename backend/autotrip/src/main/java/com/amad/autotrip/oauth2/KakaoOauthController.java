package com.amad.autotrip.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class KakaoOauthController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    private final WebClient webClient;

    public KakaoOauthController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code) {
        // 1. 인가 코드를 사용해 액세스 토큰 요청
        String tokenResponse = webClient.post()
                .uri(tokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 2. 토큰 응답에서 액세스 토큰 추출 (실제로는 JSON 파싱 필요)
        String accessToken = extractAccessToken(tokenResponse);

        // 3. 액세스 토큰으로 사용자 정보 요청
        String userInfo = webClient.get()
                .uri(userInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 4. 사용자 정보를 기반으로 로그인 처리 (예: JWT 생성 등)
        return "User Info: " + userInfo; // 실제로는 DTO로 변환해 처리
    }

    private String extractAccessToken(String tokenResponse) {
        // JSON 파싱 로직 추가 필요 (예: Jackson 사용)
        // 여기서는 단순히 예제용으로 "access_token" 문자열 반환
        return "parsed_access_token";
    }
}
