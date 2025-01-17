package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.repository.AccessTokenRepository;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.AccessTokenDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class AccessTokenAccessServiceImpl implements AccessTokenAccessService {

    private final AccessTokenRepository accessTokenRepository;
    private final AccessTokenDtoMapper modelMapper;

    @Autowired
    AccessTokenAccessServiceImpl(AccessTokenRepository accessTokenRepository, AccessTokenDtoMapper modelMapper) {
        this.accessTokenRepository = accessTokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Optional<AccessTokenDto> findByToken(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        return accessTokenRepository.findByToken(accessToken).map(modelMapper::convertToDestination);
    }

    @Override
    @Transactional
    public Optional<AccessTokenDto> findById(Long id) {
        Assert.notNull(id, "The 'id' must not be null!");
        return accessTokenRepository.findById(id).map(modelMapper::convertToDestination);
    }

    @Override
    @Transactional
    public AccessTokenDto save(AccessTokenDto dto) {
        Assert.notNull(dto, "The 'dto' must not be null!");
        var entity = modelMapper.convertToSource(dto);
        return modelMapper.convertToDestination(accessTokenRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Assert.notNull(id, "The 'id' must not be null!");
        accessTokenRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<AccessTokenDto> dtos) {
        Assert.notNull(dtos, "The 'dtos' must not be null!");
        if (dtos.isEmpty()) {
            LOGGER.info("A collection of access tokens is empty. Do nothing. Return.");
            return;
        }
        var entities =
                dtos.stream()
                        .map(modelMapper::convertToSource)
                        .collect(Collectors.toSet());
        accessTokenRepository.deleteAll(entities);
    }
}