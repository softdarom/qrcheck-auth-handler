package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.repository.UserRepository;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.UserDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserDto;

import java.util.Optional;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class UserAccessServiceImpl implements UserAccessService {

    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;

    @Autowired
    UserAccessServiceImpl(UserRepository userRepository, UserDtoMapper userMapper) {
        this.userRepository = userRepository;
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
    public UserDto save(UserDto dto) {
        Assert.notNull(dto, "The 'dto' must not be null!");
        var user = userMapper.convertToSource(dto);
        return userMapper.convertToDestination(userRepository.save(user));
    }
}