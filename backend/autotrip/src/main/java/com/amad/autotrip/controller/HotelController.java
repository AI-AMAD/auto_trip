package com.amad.autotrip.controller;


import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/get/lastplace")
    public TripScheduleDto getLastPlace(@RequestParam String username) {
        return hotelService.getLastPlace(username);
    }

    @PostMapping("/get/nearby-hotels")
    public Mono<ResponseEntity> searchHotels(@RequestBody) {
        @Value("${google.api.key}")
        private String apiKey;

        // 추천 맛집 검색 결과 반환
        public List<GetRecommendedPlacesResponseDTO> getAllRecommendedPlace(Country country, City city){
            List<GetRecommendedPlacesResponseDTO> result = new ArrayList<>();

            // "{국가} {도시} 맛집" 검색 키워드 구성
            String searchKeyword = country.toString().concat(" ").concat(city.toString()).concat(" 맛집");
            // Google Maps API 키 등록
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            try{
                // Text Search API 호출
                PlacesSearchResponse response = PlacesApi.textSearchQuery(context, searchKeyword).await();
                if(response.results != null && response.results.length > 0){
                    // 검색한 모든 장소들에 대해, 장소 세부정보 API 호출
                    for(PlacesSearchResult res : response.results){
                        GetRecommendedPlacesResponseDTO place = getPlaceDetails(res.placeId);
                        if(place != null) result.add(place);
                    }
                }
                else log.info("No place found : "+searchKeyword);
            }
            catch (Exception e){
                throw new GoogleApiException();
            }
            return result;
        }

        // 장소 세부정보 API
        private GetRecommendedPlacesResponseDTO getPlaceDetails(String placeId){
            // Google Maps API 키 등록
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            String photoUrl;

            try{
                // Details API 호출
                PlaceDetails details = PlacesApi.placeDetails(context, placeId).language("ko").await();
                // 이미지가 없는 장소는 결과에 포함하지 않음
                if(details.photos == null)
                    return null;
                else {
                    // 장소 이미지를 url로 저장 (maxwidth = 500 설정)
                    photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="
                            .concat(details.photos[1].photoReference) // 장소에 있는 두 번째 이미지
                            .concat("&key=")
                            .concat(apiKey);
                }
                return new GetRecommendedPlacesResponseDTO(
                        details.placeId,
                        details.name,
                        details.url.toString(),
                        details.rating,
                        photoUrl
                );
            }catch (Exception e){
                throw new GoogleApiException();
            }
        }
    }
}
