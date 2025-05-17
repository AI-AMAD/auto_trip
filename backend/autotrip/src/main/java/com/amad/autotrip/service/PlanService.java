package com.amad.autotrip.service;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.mybatis.PlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

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
        CATEGORY_MAPPING.put("박물관", "박물관");
        // 필요시 추가 매핑 가능, 예: CATEGORY_MAPPING.put("박물관", "미술관");
    }

    public PlanService(PlanMapper planMapper, WebClient.Builder webClientBuilder) {
        this.planMapper = planMapper;
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
    }

    // 네이버 검색 API로 장소 검색 및 이미지 URL 가져오기
    public Mono<List<PlaceWithImage>> naverSearch(String username, String place, List<String> settings) {
        if (settings == null || settings.isEmpty()) {
            return Mono.just(new ArrayList<>());
        }

        log.info("{}님의 auto_trip START --------------------", username);

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
                .distinct(item -> item.getAddress()) // 중복 장소 제거
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
    @Transactional
    public Mono<Long> createAndSaveTripPlan(TripSummaryDto tripSummaryDto, List<PlaceWithImage> places, List<String> settings) {
        // TripPlan 생성
        TripPlanDto tripPlan = new TripPlanDto();

        tripPlan.setUsername(tripSummaryDto.getUsername());
        tripPlan.setPlace(tripSummaryDto.getPlace());
        tripPlan.setStartYmd(tripSummaryDto.getStartYmd());
        tripPlan.setEndYmd(tripSummaryDto.getEndYmd());
        tripPlan.setSettings(String.join(",", settings));

        // TripPlan 저장
        planMapper.insertTripPlan(tripPlan);
        Long tripId = tripPlan.getTripId();

        // 기존 TripSchedule 삭제
        int deletedRows = planMapper.deleteTripSchedulesByTripId(tripId);
        log.info("기존 스케줄 삭제 완료: tripId={}, deletedRows={}", tripId, deletedRows);

        // 일정 배치
        Map<String, List<TripScheduleDto>> scheduleByDate = arrangeSchedule(tripId, tripSummaryDto.getStartYmd(),
                tripSummaryDto.getEndYmd(), places, settings);

        // 4. TripSchedule 저장
        scheduleByDate.values().forEach(schedules ->
                schedules.forEach(planMapper::insertTripSchedule));

        log.info("{}님의 auto_trip END --------------------", tripSummaryDto.getUsername());

        return Mono.just(tripId);
    }

    // 일정 배치 로직
    private Map<String, List<TripScheduleDto>> arrangeSchedule(Long tripId, String startDate, String endDate,
                                                               List<PlaceWithImage> places, List<String> settings) {
        Map<String, List<TripScheduleDto>> scheduleByDate = new HashMap<>();
        scheduleByDate.put(startDate, new ArrayList<>());
        scheduleByDate.put(endDate, new ArrayList<>());

        // 사용된 장소 추적
        Set<String> usedPlaces = new HashSet<>();

//        // 첫째 날: settings 순서대로 일정 배치
//        int order = 1;
//        for (String category : settings) {
//            for (PlaceWithImage place : places) {
//                String filterCategory = CATEGORY_MAPPING.getOrDefault(category, category);
//                if (place.getPlace().getCategory().contains(filterCategory) &&
//                        !usedPlaces.contains(place.getPlace().getTitle())) {
//                    TripScheduleDto schedule = new TripScheduleDto();
//                    schedule.setTripId(tripId);
//                    schedule.setStartYmd(startDate);
//                    schedule.setActivityOrder(order++);
//                    schedule.setActivityType(category);
//                    schedule.setActivityName(place.getPlace().getTitle().replaceAll("<[^>]+>", ""));
//                    schedule.setActivityAddress(place.getPlace().getAddress());
//                    schedule.setActivityImageUrl(place.getImageUrl());
//                    scheduleByDate.get(startDate).add(schedule);
//                    log.info("첫째 날 일정: startYmd={}, category={}, activity={}",
//                            startDate, category, schedule.getActivityName());
//                    usedPlaces.add(place.getPlace().getTitle());
//                    break;
//                }
//            }
//        }

        // [ADDED] 새로운 첫째 날 로직
        List<String> nonFoodCategories = new ArrayList<>();
        for (String category : settings) {
            if (!category.equals("맛집") && !category.equals("카페")) {
                nonFoodCategories.add(category);
            }
        }
        List<String> firstDayCategories = new ArrayList<>();
        if (!nonFoodCategories.isEmpty()) {
            Collections.shuffle(nonFoodCategories);
            firstDayCategories.add(nonFoodCategories.get(0));
            nonFoodCategories.remove(0);
        }
        if (settings.contains("맛집")) {
            firstDayCategories.add("맛집");
        }
        if (settings.contains("카페")) {
            firstDayCategories.add("카페");
        }
        firstDayCategories.addAll(nonFoodCategories);

        int order = 1;
        for (String category : firstDayCategories) {
            for (PlaceWithImage place : places) {
                String filterCategory = CATEGORY_MAPPING.getOrDefault(category, category);
                if (place.getPlace().getCategory().contains(filterCategory) &&
                        !usedPlaces.contains(place.getPlace().getTitle())) {
                    TripScheduleDto schedule = new TripScheduleDto();
                    schedule.setTripId(tripId);
                    schedule.setStartYmd(startDate);
                    schedule.setActivityOrder(order++);
                    schedule.setActivityType(category);
                    schedule.setActivityName(place.getPlace().getTitle().replaceAll("<[^>]+>", ""));
                    schedule.setActivityAddress(place.getPlace().getAddress());
                    schedule.setActivityImageUrl(place.getImageUrl());
                    scheduleByDate.get(startDate).add(schedule);
                    log.info("첫째 날 일정: startYmd={}, category={}, activity={}",
                            startDate, category, schedule.getActivityName());
                    usedPlaces.add(place.getPlace().getTitle());
                    break;
                }
            }
        }

//        // 둘째 날: settings를 역순으로 배치 (또는 다른 순서로 조정 가능)
//        order = 1;
//        List<String> reversedSettings = new ArrayList<>(settings);
//        Collections.reverse(reversedSettings); // settings 역순
//        for (String category : reversedSettings) {
//            for (PlaceWithImage place : places) {
//                String filterCategory = CATEGORY_MAPPING.getOrDefault(category, category);
//                if (place.getPlace().getCategory().contains(filterCategory) &&
//                        !usedPlaces.contains(place.getPlace().getTitle())) {
//                    TripScheduleDto schedule = new TripScheduleDto();
//                    schedule.setTripId(tripId);
//                    schedule.setEndYmd(endDate);
//                    schedule.setActivityOrder(order++);
//                    schedule.setActivityType(category);
//                    schedule.setActivityName(place.getPlace().getTitle().replaceAll("<[^>]+>", ""));
//                    schedule.setActivityAddress(place.getPlace().getAddress());
//                    schedule.setActivityImageUrl(place.getImageUrl());
//                    scheduleByDate.get(endDate).add(schedule);
//                    log.info("둘째 날 일정: endYmd={}, category={}, activity={}",
//                            endDate, category, schedule.getActivityName());
//                    usedPlaces.add(place.getPlace().getTitle());
//                    break;
//                }
//            }
//        }

        // [ADDED] 새로운 둘째 날 로직
        nonFoodCategories = new ArrayList<>();
        for (String category : settings) {
            if (!category.equals("맛집") && !category.equals("카페")) {
                nonFoodCategories.add(category);
            }
        }
        List<String> secondDayCategories = new ArrayList<>();
        if (!nonFoodCategories.isEmpty()) {
            Collections.shuffle(nonFoodCategories);
            secondDayCategories.add(nonFoodCategories.get(0));
            nonFoodCategories.remove(0);
        }
        if (settings.contains("맛집")) {
            secondDayCategories.add("맛집");
        }
        if (settings.contains("카페")) {
            secondDayCategories.add("카페");
        }
        secondDayCategories.addAll(nonFoodCategories);

        order = 1;
        for (String category : secondDayCategories) {
            for (PlaceWithImage place : places) {
                String filterCategory = CATEGORY_MAPPING.getOrDefault(category, category);
                if (place.getPlace().getCategory().contains(filterCategory) &&
                        !usedPlaces.contains(place.getPlace().getTitle())) {
                    TripScheduleDto schedule = new TripScheduleDto();
                    schedule.setTripId(tripId);
                    schedule.setEndYmd(endDate);
                    schedule.setActivityOrder(order++);
                    schedule.setActivityType(category);
                    schedule.setActivityName(place.getPlace().getTitle().replaceAll("<[^>]+>", ""));
                    schedule.setActivityAddress(place.getPlace().getAddress());
                    schedule.setActivityImageUrl(place.getImageUrl());
                    scheduleByDate.get(endDate).add(schedule);
                    log.info("둘째 날 일정: endYmd={}, category={}, activity={}",
                            endDate, category, schedule.getActivityName());
                    usedPlaces.add(place.getPlace().getTitle());
                    break;
                }
            }
        }

        return scheduleByDate;
    }

}
