package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.AccessTokenEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.AccessTokenDto;

@Component
public class AccessTokenDtoMapper extends AbstractDtoMapper<AccessTokenEntity, AccessTokenDto> {

    @Autowired
    protected AccessTokenDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }
}