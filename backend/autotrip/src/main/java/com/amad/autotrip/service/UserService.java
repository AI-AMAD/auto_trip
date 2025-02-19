package com.amad.autotrip.service;

import com.amad.autotrip.dto.Users;
import com.amad.autotrip.mybatis.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UsersMapper usersMapper;

    public UserService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    public Boolean existCheck(String id) {
        return usersMapper.findByUserId(id) != null;
    }

    public String join(Users user) {

        String id = user.getId();

        Boolean isExist = existCheck(id);

        if (!isExist) {
            log.info("회원가입 진행 해도 됨");
        } else {
            log.info("이미 존재하는 회원 입니다.");
        }

        return "회원 가입이 정상적으로 이루어졌습니다.";
    }
}
