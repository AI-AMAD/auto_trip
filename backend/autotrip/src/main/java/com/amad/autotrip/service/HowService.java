package com.amad.autotrip.service;

import com.amad.autotrip.dto.ActivityDto;
import com.amad.autotrip.dto.ActivityWithDateDto;
import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.mybatis.HowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class HowService {

    private final HowMapper howMapper;

    public HowService(HowMapper howMapper) {
        this.howMapper = howMapper;
    }

    public List<ScheduleDto> getTripPlanByUsername(String username) {
        List<ScheduleDto> plans = howMapper.findTripPlanByUsername(username);
        return plans.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    private ScheduleDto convertToMap(ScheduleDto dto) {
        Map<String, List<ActivityDto>> startYmd = new HashMap<>();
        Map<String, List<ActivityDto>> endYmd = new HashMap<>();

        if (dto.getActivities() != null) {
            // activities를 startYmd와 endYmd로 분리
            dto.getActivities().forEach(activity -> {
                Map<String, List<ActivityDto>> targetMap = activity.getDate().equals(dto.getActivities().stream()
                        .filter(a -> a.getDate() != null)
                        .findFirst()
                        .map(ActivityWithDateDto::getDate)
                        .orElse(null)) ? startYmd : endYmd;

                List<ActivityDto> activityList = targetMap.computeIfAbsent(activity.getDate(), k -> new ArrayList<>());
                activityList.add(ActivityDto.builder()
                        .activityOrder(activity.getActivityOrder())
                        .activityType(activity.getActivityType())
                        .activityName(activity.getActivityName())
                        .activityAddress(activity.getActivityAddress())
                        .activityImageUrl(activity.getActivityImageUrl())
                        .build());
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
}
