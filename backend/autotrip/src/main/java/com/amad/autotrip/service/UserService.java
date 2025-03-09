package com.amad.autotrip.service;

import java.util.Collections;
import com.amad.autotrip.dto.UserDto;
import com.amad.autotrip.entity.Authority;
import com.amad.autotrip.entity.Users;
import com.amad.autotrip.exception.DuplicateMemberException;
import com.amad.autotrip.exception.NotFoundMemberException;
import com.amad.autotrip.repository.AuthorityRepository;
import com.amad.autotrip.repository.UserRepository;
import com.amad.autotrip.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

//        Authority authority = Authority.builder()
//                .authorityName("ROLE_USER")
//                .build();

        Authority authority = authorityRepository.findById("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(Authority.builder().authorityName("ROLE_USER").build()));

        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
