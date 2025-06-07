package com.amad.autotrip.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExistMapper {

    int existWhereInfo(String username);

    int existScheduleInfo(String username);

    int existFinalInfo(String username);
}
