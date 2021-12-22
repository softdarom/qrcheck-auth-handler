package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.repository.RoleRepository;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.RoleDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RoleDto;

import java.util.Set;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class RoleAccessServiceImpl implements RoleAccessService {

    private final RoleRepository roleRepository;
    private final RoleDtoMapper modelMapper;

    @Autowired
    RoleAccessServiceImpl(RoleRepository roleRepository, RoleDtoMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public RoleDto defaultRole() {
        return roleRepository.findByName(RoleType.USER).map(modelMapper::convertToDestination)
                .orElseThrow(() -> new NotFoundException("Roles not found by " + Set.of(RoleType.USER)));
    }

    @Transactional
    @Override
    public Set<RoleDto> findByUserId(Long userId) {
        Assert.notNull(userId, "The 'userId' must not be null!");
        return modelMapper.convertToDestinations(roleRepository.findAllByUserId(userId));
    }

    @Transactional
    @Override
    public Set<RoleDto> findByExternalUserId(Long externalUserId) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not be null!");
        return modelMapper.convertToDestinations(roleRepository.findAllByExternalUserId(externalUserId));
    }
}