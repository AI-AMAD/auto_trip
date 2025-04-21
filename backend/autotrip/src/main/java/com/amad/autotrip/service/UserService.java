package com.amad.autotrip.service;

import java.util.Collections;

import com.amad.autotrip.dto.LoginDto;
import com.amad.autotrip.dto.TokenDto;
import com.amad.autotrip.dto.UserDto;
import com.amad.autotrip.dto.UserProfileDto;
import com.amad.autotrip.entity.Authority;
import com.amad.autotrip.entity.Users;
import com.amad.autotrip.exception.DuplicateMemberException;
import com.amad.autotrip.exception.NotFoundMemberException;
import com.amad.autotrip.jwt.JwtFilter;
import com.amad.autotrip.jwt.TokenProvider;
import com.amad.autotrip.repository.AuthorityRepository;
import com.amad.autotrip.repository.UserRepository;
import com.amad.autotrip.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserService(UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder) {

        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("userService 에서 생성한 에러 문구 입니다 -> 이미 가입되어 있는 유저입니다.");
        }

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

    public UserProfileDto getUserProfile(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("사용자 없음: " + username));
        return new UserProfileDto(user.getUserId(), user.getNickname(), user.getProfileImageUrl());
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

    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
