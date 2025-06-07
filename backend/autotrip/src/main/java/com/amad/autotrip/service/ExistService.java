package com.amad.autotrip.service;

import com.amad.autotrip.mybatis.ExistMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ExistService {

    private final ExistMapper existMapper;

    public ExistService(ExistMapper existMapper) {
        this.existMapper = existMapper;
    }

    public boolean existWhereInfo(String username) {
        return existMapper.existWhereInfo(username) > 0;
    }

    public boolean existScheduleInfo(String username) {
        return existMapper.existScheduleInfo(username) > 0;
    }

    public boolean existFinalInfo(String username) {
        return existMapper.existFinalInfo(username) > 0;
    }
}
