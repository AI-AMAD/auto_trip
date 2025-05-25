package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.ScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HowMapper {
    List<ScheduleDto> findTripPlanByUsername(String username);
}
