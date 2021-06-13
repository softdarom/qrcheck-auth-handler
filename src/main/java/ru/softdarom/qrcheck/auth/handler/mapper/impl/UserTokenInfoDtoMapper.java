package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserTokenInfoEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserTokenInfoDto;

@Component
public class UserTokenInfoDtoMapper extends AbstractDtoMapper<UserTokenInfoEntity, UserTokenInfoDto> {

    @Autowired
    protected UserTokenInfoDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }
}