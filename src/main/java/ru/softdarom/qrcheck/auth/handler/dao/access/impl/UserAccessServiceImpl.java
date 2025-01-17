package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.repository.RoleRepository;
import ru.softdarom.qrcheck.auth.handler.dao.repository.UserRepository;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.UserDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserDto;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class UserAccessServiceImpl implements UserAccessService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper userMapper;

    @Autowired
    UserAccessServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserDtoMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public Optional<UserDto> findByExternalUserId(Long externalUserId) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not be null!");
        return userRepository.findByExternalUserId(externalUserId).map(userMapper::convertToDestination);
    }

    @Transactional
    @Override
    public Set<UserDto> findByExternalUserIds(Collection<Long> externalUserIds) {
        Assert.notNull(externalUserIds, "The 'externalUserIds' must not be null!");
        if (externalUserIds.isEmpty()) {
            LOGGER.warn("The 'externalUserIds' is empty. Return an empty set!");
            return Set.of();
        }
        return userRepository.findByExternalUserIds(externalUserIds).stream()
                .map(userMapper::convertToDestination)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public UserDto save(UserDto dto) {
        Assert.notNull(dto, "The 'dto' must not be null!");
        var user = userMapper.convertToSource(dto);
        return userMapper.convertToDestination(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto changeRole(Long externalUserId, RoleType role) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not be null!");
        Assert.notNull(role, "The 'role' must not be null!");
        var newRole = roleRepository.findByName(role).orElseThrow(() -> new NotFoundException("Role not found by " + role));
        var user = userRepository.findByExternalUserId(externalUserId).orElseThrow(() -> new NotFoundException("User not found by external id " + externalUserId));
        user.setRoles(Set.of(newRole));
        return userMapper.convertToDestination(user);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        Assert.notNull(userId, "The 'userId' must not be null!");
        userRepository.deleteById(userId);
    }
}