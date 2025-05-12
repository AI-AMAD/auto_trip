package com.amad.autotrip.service;

import com.amad.autotrip.dto.NaverImageResponseDto;
import com.amad.autotrip.dto.NaverSearchResponseDto;
import com.amad.autotrip.dto.PlaceWithImage;
import com.amad.autotrip.mybatis.PlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlanService {

    private final PlanMapper planMapper;

    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    // 카테고리 매핑 맵
    private static final Map<String, String> CATEGORY_MAPPING = new HashMap<>();

    static {
        CATEGORY_MAPPING.put("관광", "명소");
        CATEGORY_MAPPING.put("레포츠", "스포츠");
        CATEGORY_MAPPING.put("맛집", "음식");
        // 필요시 추가 매핑 가능, 예: CATEGORY_MAPPING.put("박물관", "미술관");
    }

    public PlanService(PlanMapper planMapper, WebClient.Builder webClientBuilder) {
        this.planMapper = planMapper;
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
    }

    // 네이버 검색 API로 장소 검색 및 이미지 URL 가져오기
    public Mono<List<PlaceWithImage>> naverSearch(String place, List<String> settings) {
        if (settings == null || settings.isEmpty()) {
            return Mono.just(new ArrayList<>());
        }

        // settings에 따라 여러 쿼리로 검색
        return Flux.fromIterable(settings)
                .concatMap(category -> {
                    String query = place + " " + category;
                    log.info("검색 쿼리: {}", query);
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
                            .flatMapMany(response -> {
                                log.info("검색 결과: {}개 장소", response.getItems().size());
                                response.getItems().forEach(item -> log.info("장소: {}, 카테고리: {}", item.getTitle(), item.getCategory()));
                                return Flux.fromIterable(response.getItems());
                            })
                            // 검색 결과가 카테고리에 맞는지 필터링
                            .filter(item -> settings.stream().anyMatch(cat -> {
                                String filterCategory = CATEGORY_MAPPING.getOrDefault(cat, cat);
                                return item.getCategory().contains(filterCategory);
                            }))
                            .doOnNext(item -> log.info("필터링된 장소: {}, 카테고리: {}", item.getTitle(), item.getCategory()));
                }, 1) // 순차 처리
                .delayElements(Duration.ofMillis(200)) // 네이버 API 초당 호출 한도 초과로 인한 각 요청 간 200ms 딜레이
                .distinct(item -> item.getAddress())
                .concatMap(item -> {
                    String cleanTitle = item.getTitle().replaceAll("<[^>]+>", "");
                    log.info("이미지 검색: {}", cleanTitle);
                    return naverSearchImage(cleanTitle)
                            .map(imageResponse -> {
                                String imageUrl = imageResponse.getItems().isEmpty() ? null : imageResponse.getItems().get(0).getLink();
                                log.info("장소: {}, 이미지 URL: {}", cleanTitle, imageUrl);
                                return new PlaceWithImage(item, imageUrl);
                            })
                            .doOnError(e -> log.error("이미지 검색 에러: {}", e.getMessage()));
                }, 1) // 순차 처리
                .delayElements(Duration.ofMillis(200)) // 네이버 API 초당 호출 한도 초과로 인한 각 이미지 요청 간 200ms 딜레이
                .collectList()
                .doOnSuccess(list -> {
                    log.info("최종 결과: {}개 장소", list.size());
                    log.info("--------------------------");
                });
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


    // 일정 배치 및 DB 저장
    public Mono<Long> createAndSaveTripPlan(TripSummaryDto tripSummaryDto, List<PlaceWithImage> places) {
        // 1. TripPlan 생성
        TripPlan tripPlan = new TripPlan();
        tripPlan.setUsername(tripSummaryDto.getUsername());
        tripPlan.setLocation(tripSummaryDto.getPlace());
        tripPlan.setStartDate(tripSummaryDto.getStartDate());
        tripPlan.setEndDate(tripSummaryDto.getEndDate());

        List<String> settings = new ArrayList<>();
        if (tripSummaryDto.isActivity()) settings.add("레포츠");
        if (tripSummaryDto.isMuseum()) settings.add("박물관");
        if (tripSummaryDto.isCafe()) settings.add("카페");
        if (tripSummaryDto.isTourAtt()) settings.add("관광");
        settings.add("맛집");
        tripPlan.setSettings(String.join(",", settings));

        // 2. TripPlan 저장
        planMapper.insertTripPlan(tripPlan);
        Long tripId = tripPlan.getTripId();

        // 3. 일정 배치
        Map<String, List<TripSchedule>> scheduleByDate = arrangeSchedule(tripId, tripSummaryDto.getStartDate(),
                tripSummaryDto.getEndDate(), places, settings);

        // 4. TripSchedule 저장
        scheduleByDate.values().forEach(schedules ->
                schedules.forEach(planMapper::insertTripSchedule));

        return Mono.just(tripId);
    }

    // 일정 배치 로직
    private Map<String, List<TripSchedule>> arrangeSchedule(Long tripId, String startDate, String endDate,
                                                            List<PlaceWithImage> places, List<String> settings) {
        Map<String, List<TripSchedule>> scheduleByDate = new HashMap<>();
        scheduleByDate.put(startDate, new ArrayList<>());
        scheduleByDate.put(endDate, new ArrayList<>());

        // 사용된 장소 추적
        Set<String> usedPlaces = new HashSet<>();

        // 첫째 날: 명소 → 카페 → 맛집 → 박물관
        int order = 1;
        for (String category : Arrays.asList("관광", "카페", "맛집", "박물관")) {
            if (settings.contains(category)) {
                for (PlaceWithImage place : places) {
                    String filterCategory = CATEGORY_MAPPING.getOrDefault(category, category);
                    if (place.getItem().getCategory().contains(filterCategory) &&
                            !usedPlaces.contains(place.getItem().getTitle())) {
                        TripSchedule schedule = new TripSchedule();
                        schedule.setTripId(tripId);
                        schedule.setDate(startDate);
                        schedule.setActivityOrder(order++);
                        schedule.setActivityType(category);
                        schedule.setActivityName(place.getItem().getTitle().replaceAll("<[^>]+>", ""));
                        schedule.setActivityAddress(place.getItem().getAddress());
                        schedule.setActivityImageUrl(place.getImageUrl());
                        scheduleByDate.get(startDate).add(schedule);
                        usedPlaces.add(place.getItem().getTitle());
                        break;
                    }
                }
            }
        }

        // 둘째 날: 박물관 → 명소 → 카페
        order = 1;
        for (String category : Arrays.asList("박물관", "관광", "카페")) {
            if (settings.contains(category)) {
                for (PlaceWithImage place : places) {
                    String filterCategory = CATEGORY_MAPPING.getOrDefault(category,cault
                    if (place.getItem().getCategory().contains(filterCategory) &&
                            !usedPlaces.contains(place.getItem().getTitle())) {
                        TripSchedule schedule = new TripSchedule();
                        schedule.setTripId(tripId);
                        schedule.setDate(endDate);
                        schedule.setActivityOrder(order++);
                        schedule.setActivityType(category);
                        schedule.setActivityName(place.getItem().getTitle().replaceAll("<[^>]+>", ""));
                        schedule.setActivityAddress(place.getItem().getAddress());
                        schedule.setActivityImageUrl(place.getImageUrl());
                        scheduleByDate.get(endDate).add(schedule);
                        usedPlaces.add(place.getItem().getTitle());
                        break;
                    }
                }
            }
        }

        return scheduleByDate;
    }

}
