package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

    Integer findByUserId(String id);

    Integer register(Users user);
}
