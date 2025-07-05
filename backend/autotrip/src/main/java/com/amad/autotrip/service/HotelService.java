package com.amad.autotrip.service;

import com.amad.autotrip.dto.*;
import com.amad.autotrip.mybatis.HotelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HotelService {

    private final HotelMapper hotelMapper;
    private final WebClient naverWebClient;
    private final WebClient googleWebClient;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${google.api.key}")
    private String googleApiKey;

    public HotelService(HotelMapper hotelMapper, WebClient.Builder webClientBuilder) {
        this.hotelMapper = hotelMapper;
        this.naverWebClient = webClientBuilder.baseUrl("https://openapi.naver.com").build();
        this.googleWebClient = webClientBuilder.baseUrl("https://places.googleapis.com").build();
    }

    public TripScheduleDto getLastPlace(String username) {
        return hotelMapper.getLastPlace(username);
    }

    // Google Maps API로 호텔 3개 검색
    public Mono<List<SearchPlaceDto>> searchHotelsFromGoogle(String query) {
        String url = "/v1/places:searchText";
        return googleWebClient.post()
                .uri(url)
                .header("X-Goog-Api-Key", googleApiKey)
                .header("X-Goog-FieldMask", "places.displayName,places.formattedAddress")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"textQuery\": \"" + query + "\", \"languageCode\": \"ko\", \"pageSize\": 3}")
                .retrieve()
                .bodyToMono(TextSearchResponseDto.class)
                .map(TextSearchResponseDto::getPlaces)
                .onErrorResume(e -> {
                    log.error("Google API 호출 에러: {}", e.getMessage());
                    return Mono.just(new ArrayList<>());
                });
    }

    // Naver Image API로 이미지 검색
    public Mono<String> searchImageFromNaver(String placeName) {
        return naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/image")
                        .queryParam("query", placeName)
                        .queryParam("sort", "sim")
                        .build())
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .retrieve()
                .bodyToMono(NaverImageResponseDto.class)
                .map(response -> response.getItems().isEmpty() ? null : response.getItems().get(0).getLink())
                .onErrorResume(e -> {
                    log.error("Naver Image API 호출 에러: {}", e.getMessage());
                    return Mono.just(null);
                });
    }

    // 호텔 검색 및 이미지 가져오기
    public Mono<List<HotelWithImage>> getHotelsWithImages(String query) {
        return searchHotelsFromGoogle(query)
                .flatMapMany(Flux::fromIterable)
                .concatMap(place -> searchImageFromNaver(place.getDisplayName().getText())
                        .map(imageUrl -> {
                            HotelWithImage hotel = new HotelWithImage();
                            hotel.setName(place.getDisplayName().getText());
                            hotel.setAddress(place.getFormattedAddress());
                            hotel.setImageUrl(imageUrl);
                            return hotel;
                        }))
                .collectList();
    }

    @Transactional
    public void saveHotel(SaveHotelRequestDto request) {
        hotelMapper.upsertAccommodation(request);
    }
}
