package com.amad.autotrip.service;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.mybatis.HowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HowService {

    private final HowMapper howMapper;

    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public HowService(HowMapper howMapper, WebClient.Builder webClientBuilder) {
        this.howMapper = howMapper;
        this.webClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
    }

    public List<ScheduleDto> getTripPlanByUsername(String username) {
        List<ScheduleDto> plans = howMapper.findTripPlanByUsername(username);
        return plans.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    private ScheduleDto convertToMap(ScheduleDto dto) {
        Map<String, List<ActivityDto>> startYmd = new HashMap<>();
        Map<String, List<ActivityDto>> endYmd = new HashMap<>();

        if (dto.getActivities() != null) {
            dto.getActivities().forEach(activity -> {
                if (activity.getDate() != null && activity.getDateType() != null) {
                    Map<String, List<ActivityDto>> targetMap = "start".equals(activity.getDateType()) ? startYmd : endYmd;
                    List<ActivityDto> activityList = targetMap.computeIfAbsent(activity.getDate(), k -> new ArrayList<>());
                    activityList.add(ActivityDto.builder()
                            .scheduleId(activity.getScheduleId())
                            .activityOrder(activity.getActivityOrder())
                            .activityType(activity.getActivityType())
                            .activityName(activity.getActivityName())
                            .activityAddress(activity.getActivityAddress())
                            .activityImageUrl(activity.getActivityImageUrl())
                            .build());
                }
            });
        }

        return ScheduleDto.builder()
                .tripId(dto.getTripId())
                .username(dto.getUsername())
                .place(dto.getPlace())
                .settings(dto.getSettings())
                .startYmd(startYmd)
                .endYmd(endYmd)
                .build();
    }

    public List<PlaceWithImage> searchNewPlaces(String place, String activityType, List<ExcludedPlaceDto> excludedPlaces) {
        // 검색 쿼리 생성
        String query = place + " " + activityType;  // 예: "서울 박물관"
        log.info("검색 쿼리: {}", query);

        // 네이버 로컬 검색 API 호출
        NaverSearchResponseDto response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/local.json")
                        .queryParam("query", query)
                        .queryParam("display", 10)
                        .queryParam("sort", "comment")
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverSearchResponseDto.class)
                .block();

        // 중복되지 않은 장소 필터링
        List<PlaceWithImage> newPlaces = new ArrayList<>();
        if (response != null && response.getItems() != null) {
            for (NaverSearchResponseDto.Item item : response.getItems()) {
                String cleanTitle = item.getTitle().replaceAll("<[^>]+>", "");  // HTML 태그 제거
                boolean isExcluded = excludedPlaces.stream()
                        .anyMatch(excluded -> excluded.getName().equals(cleanTitle) &&
                                excluded.getAddress().equals(item.getAddress()));
                if (!isExcluded) {
                    // HTML 태그 제거된 title로 새로운 Item 객체 생성
                    NaverSearchResponseDto.Item cleanedItem = NaverSearchResponseDto.Item.builder()
                            .title(cleanTitle)
                            .link(item.getLink())
                            .category(item.getCategory())
                            .description(item.getDescription())
                            .telephone(item.getTelephone())
                            .address(item.getAddress())
                            .roadAddress(item.getRoadAddress())
                            .mapx(item.getMapx())
                            .mapy(item.getMapy())
                            .build();

                    NaverImageResponseDto imageResponse = naverSearchImage(cleanTitle).block();
                    String imageUrl = (imageResponse != null && !imageResponse.getItems().isEmpty())
                            ? imageResponse.getItems().get(0).getLink()
                            : null;
                    newPlaces.add(new PlaceWithImage(cleanedItem, imageUrl));
                }
            }
        }

        log.info("새로운 장소 검색 완료: {}개", newPlaces.size());
        return newPlaces;
    }

    // 이미지 검색 헬퍼 메소드
    private Mono<NaverImageResponseDto> naverSearchImage(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/image")
                        .queryParam("query", query)
                        .queryParam("display", 1)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverImageResponseDto.class);
    }

    @Transactional
    public void deleteSchedules(String username, Long tripId) {
        howMapper.deleteSchedules(username, tripId);
    }

    @Transactional
    public void createSchedules(String username, List<TripScheduleDto> schedules) {
        howMapper.insertSchedules(schedules);
    }

    @Transactional
    public void updateFinalYn(Long tripId) {
        howMapper.updateFinalYn(tripId);
    }
}
