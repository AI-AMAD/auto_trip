package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.SettingDto;
import com.amad.autotrip.dto.TripSummaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface SettingMapper {

    void saveSetting(SettingDto settingDto);

    TripSummaryDto findSettingByUsername(String username);
}
