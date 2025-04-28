package com.amad.autotrip.mybatis;

import com.amad.autotrip.dto.SettingDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingMapper {

    void saveSetting(SettingDto settingDto);
}
