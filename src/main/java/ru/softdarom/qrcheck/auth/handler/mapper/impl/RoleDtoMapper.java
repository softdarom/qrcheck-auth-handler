package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RoleDto;

@Component
public class RoleDtoMapper extends AbstractDtoMapper<RoleEntity, RoleDto> {

    @Autowired
    protected RoleDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }
}