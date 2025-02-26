package com.amad.autotrip.service;

import com.amad.autotrip.dto.Users;
import com.amad.autotrip.mybatis.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UsersMapper usersMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UsersMapper usersMapper,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersMapper = usersMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Boolean existCheck(String id) {
        return usersMapper.existByUserId(id) != null;
    }

    public void registerUser(Users user) {
        usersMapper.register(user);
    }

    public Boolean join(Users user) {

        String id = user.getId();
        String password = user.getPassword();
        String name = user.getName();
        String cellPhone = user.getCellPhone();
        String mailAddress = user.getMailAddress();

        Boolean isExist = existCheck(id);

        if (!isExist) {

            Users joinUser = new Users();
            joinUser.setId(id);
            joinUser.setPassword(bCryptPasswordEncoder.encode(password));
            joinUser.setName(name);
            joinUser.setCellPhone(cellPhone);
            joinUser.setMailAddress(mailAddress);

            registerUser(joinUser);
            return true;
        } else {
            return false;
        }
    }
}
