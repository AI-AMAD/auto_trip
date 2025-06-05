package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.dto.TripScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HowMapper {
    List<ScheduleDto> findTripPlanByUsername(String username);

    List<TripScheduleDto> findTripScheduleByTripId(String username, Long tripId);

    void deleteSchedules(String username, Long tripId, List<Long> scheduleIds);

    void insertSchedules(List<TripScheduleDto> schedules);

    void updateSchedules(List<TripScheduleDto> schedules);
}
