package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.ScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiaryMapper {

    List<ScheduleDto> findDiaryByUsername(String username);
}
