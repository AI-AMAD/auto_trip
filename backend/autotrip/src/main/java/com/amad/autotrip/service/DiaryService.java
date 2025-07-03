package com.amad.autotrip.service;

import com.amad.autotrip.dto.ActivityDto;
import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.mybatis.DiaryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryMapper diaryMapper;

    public DiaryService(DiaryMapper diaryMapper) {
        this.diaryMapper = diaryMapper;
    }


    public List<ScheduleDto> getDiaryByUsername(String username) {
        List<ScheduleDto> plans = diaryMapper.findDiaryByUsername(username);
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
                .hotel(dto.getHotel())
                .startYmd(startYmd)
                .endYmd(endYmd)
                .build();
    }
}
