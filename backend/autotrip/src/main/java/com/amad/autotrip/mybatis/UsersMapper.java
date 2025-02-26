package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.Login;
import com.amad.autotrip.dto.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

    Integer existByUserId(String id);

    Integer register(Users user);

    Login findByUserId(String id);
}
