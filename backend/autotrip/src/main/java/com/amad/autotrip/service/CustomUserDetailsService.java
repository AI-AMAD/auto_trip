package com.amad.autotrip.service;

import com.amad.autotrip.dto.CustomUsersDetails;
import com.amad.autotrip.dto.Users;
import com.amad.autotrip.mybatis.UsersMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersMapper usersMapper;

    public CustomUserDetailsService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Users userData = usersMapper.findByUserId(userId);

        if (userData != null) {
            return new CustomUsersDetails(userData);
        }

        return null;
    }
}
