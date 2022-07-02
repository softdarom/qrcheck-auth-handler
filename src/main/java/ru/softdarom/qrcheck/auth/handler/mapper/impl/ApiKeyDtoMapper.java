package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.ApiKeyEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;

@Component
public class ApiKeyDtoMapper extends AbstractDtoMapper<ApiKeyEntity, ApiKeyDto> {

    @Autowired
    protected ApiKeyDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }
}