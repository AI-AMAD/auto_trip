package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.TripPlanDto;
import com.amad.autotrip.dto.TripScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {

    void insertTripPlan(TripPlanDto tripPlan);

    void insertTripSchedule(TripScheduleDto tripSchedule);

    List<TripScheduleDto> findSchedulesByTripId(Long tripId);
}
