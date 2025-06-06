package com.amad.autotrip.service;

import com.amad.autotrip.dto.ActivityDto;
import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.dto.TripScheduleDto;
import com.amad.autotrip.mybatis.HowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
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
