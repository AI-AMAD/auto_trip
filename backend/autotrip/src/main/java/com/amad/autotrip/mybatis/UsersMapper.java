package com.amad.autotrip.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {

    Integer findByUserId(String id);
}
