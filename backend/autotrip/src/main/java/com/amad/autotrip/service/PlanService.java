package com.amad.autotrip.service;

import com.amad.autotrip.dto.NaverImageResponseDto;
import com.amad.autotrip.dto.NaverSearchResponseDto;
import com.amad.autotrip.mybatis.PlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlanService {

    private final PlanMapper planMapper;

    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public PlanService(PlanMapper planMapper, WebClient.Builder webClientBuilder) {
        this.planMapper = planMapper;
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
    }

    //    // 네이버 검색 API
//    public NaverSearchResponseDto naverSearch(String place, List<String> settings) {
//        String query = place + settings.get(1);
//        Mono<NaverSearchResponseDto> response = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1/search/local.json")
//                        .queryParam("query", query)
//                        .queryParam("display", 5)
//                        .queryParam("sort", "comment")
//                        .build())
//                .header("X-Naver-Client-Id", clientId)
//                .header("X-Naver-Client-Secret", clientSecret)
//                .retrieve()
//                .bodyToMono(NaverSearchResponseDto.class);
//
//        NaverSearchResponseDto result = response.block();
//        log.info("result.getItems()--->:  {}", result.getItems());
//        log.info("result.getItems().get(0).getCategory()-->:  {}", result.getItems().get(0).getCategory());
//
//        return response.block(); // 동기 처리
//    }
//
//    // 네이버 이미지 검색 API
//    public NaverImageResponseDto naverSearchImage(String place) {
//        Mono<NaverImageResponseDto> response = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1/search/image")
//                        .queryParam("query", place)
//                        .queryParam("sort", "sim")
//                        .build())
//                .header("X-Naver-Client-Id", clientId)
//                .header("X-Naver-Client-Secret", clientSecret)
//                .retrieve()
//                .bodyToMono(NaverImageResponseDto.class);
//
//        return response.block(); // 동기 처리
//    }


    //    그록이 짜준 코드
// 네이버 검색 API로 장소 검색 및 이미지 URL 가져오기
    public Mono<List<PlaceWithImage>> naverSearch(String place, List<String> settings) {
        if (settings == null || settings.isEmpty()) {
            return Mono.just(new ArrayList<>());
        }

        // settings에 따라 여러 쿼리로 검색
        return Flux.fromIterable(settings)
                .flatMap(category -> {
                    String query = place + " " + category;
                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/v1/search/local.json")
                                    .queryParam("query", query)
                                    .queryParam("display", 5)
                                    .queryParam("sort", "comment")
                                    .build())
                            .header("X-Naver-Client-Id", clientId)
                            .header("X-Naver-Client-Secret", clientSecret)
                            .retrieve()
                            .bodyToMono(NaverSearchResponseDto.class)
                            .flatMapMany(response -> Flux.fromIterable(response.getItems()))
                            .filter(item -> settings.stream().anyMatch(cat -> item.getCategory().contains(cat)));
                })
                // 중복 장소 제거 (address 기준)
                .distinct(item -> item.getAddress())
                .flatMap(item -> {
                    // HTML 태그 제거 후 이미지 검색
                    String cleanTitle = item.getTitle().replaceAll("<[^>]+>", "");
                    return naverSearchImage(cleanTitle)
                            .map(imageResponse -> {
                                // 이미지 URL이 있는 경우 첫 번째 이미지 사용
                                String imageUrl = imageResponse.getItems().isEmpty()
                                        ? null
                                        : imageResponse.getItems().get(0).getLink();
                                return new PlaceWithImage(item, imageUrl);
                            });
                })
                .collectList();
    }

    // 네이버 이미지 검색 API
    public Mono<NaverImageResponseDto> naverSearchImage(String place) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/image")
                        .queryParam("query", place)
                        .queryParam("sort", "sim")
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverImageResponseDto.class);
    }

    // 장소와 이미지 URL을 함께 담는 DTO 클래스
    public static class PlaceWithImage {
        private final NaverSearchResponseDto.Item place;
        private final String imageUrl;

        public PlaceWithImage(NaverSearchResponseDto.Item place, String imageUrl) {
            this.place = place;
            this.imageUrl = imageUrl;
        }

        public NaverSearchResponseDto.Item getPlace() {
            return place;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
