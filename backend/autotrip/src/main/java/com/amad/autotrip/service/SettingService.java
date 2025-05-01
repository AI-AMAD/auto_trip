package com.amad.autotrip.service;

import com.amad.autotrip.dto.SettingDto;
import com.amad.autotrip.dto.TripSummaryDto;
import com.amad.autotrip.mybatis.SettingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class SettingService {

    private final SettingMapper settingMapper;

    public SettingService(SettingMapper settingMapper) {
        this.settingMapper = settingMapper;
    }

    public void saveSetting(SettingDto settingDto) {

        settingMapper.saveSetting(settingDto);
    }

    public TripSummaryDto getTripSummary(String username) {

        return settingMapper.findSettingByUsername(username);
    }
}
