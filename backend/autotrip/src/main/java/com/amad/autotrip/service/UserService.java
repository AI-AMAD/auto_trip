package com.amad.autotrip.service;

import com.amad.autotrip.dto.Users;
import com.amad.autotrip.mybatis.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

    public void registerUser(Users user) {
        usersMapper.register(user);
    }

    public Boolean join(Users user) {

        String id = user.getId();

        Boolean isExist = existCheck(id);

        if (!isExist) {
            registerUser(user);
            return true;
        } else {
            return false;
        }
    }
}
