package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.ScheduleDto;
import com.amad.autotrip.dto.TripScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HowMapper {
    List<ScheduleDto> findTripPlanByUsername(String username);

    void deleteSchedules(String username, Long tripId);

    void insertSchedules(List<TripScheduleDto> schedules);
}
