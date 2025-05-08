package com.amad.autotrip.service;

import com.amad.autotrip.dto.NaverImageResponseDto;
import com.amad.autotrip.dto.NaverSearchResponseDto;
import com.amad.autotrip.mybatis.PlanMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PlanService {

    private final PlanMapper planMapper;

    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

//    public NaverSearchService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
//    }

    public PlanService(PlanMapper planMapper, WebClient.Builder webClientBuilder) {
        this.planMapper = planMapper;
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
    }

    // 네이버 검색 API
    public NaverSearchResponseDto naverSearch(String place) {
        Mono<NaverSearchResponseDto> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/local.json")
                        .queryParam("query", place)
                        .queryParam("display", 5)
                        .queryParam("sort", "comment")
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverSearchResponseDto.class);

        return response.block(); // 동기 처리
    }

    // 네이버 이미지 검색 API
    public NaverImageResponseDto naverSearchImage(String place) {
        Mono<NaverImageResponseDto> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/image")
                        .queryParam("query", place)
                        .queryParam("sort", "sim")
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverImageResponseDto.class);

        return response.block(); // 동기 처리
    }
}
