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
        return existMapper.ExistWhereInfo(username) > 0;
    }
}
