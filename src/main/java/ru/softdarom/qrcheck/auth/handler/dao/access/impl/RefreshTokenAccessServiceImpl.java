package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.repository.RefreshTokenRepository;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.RefreshTokenDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class RefreshTokenAccessServiceImpl implements RefreshTokenAccessService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenDtoMapper modelMapper;

    @Autowired
    RefreshTokenAccessServiceImpl(RefreshTokenRepository refreshTokenRepository, RefreshTokenDtoMapper modelMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Set<RefreshTokenDto> find(Long userId, ProviderType provider) {
        Assert.notNull(userId, "The 'userId' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        var refreshTokens = refreshTokenRepository.findAllByUserIdAndProvider(userId, provider)
                .stream()
                .filter(it -> Objects.equals(it.getActive(), ActiveType.ENABLED))
                .collect(Collectors.toSet());
        return modelMapper.convertToDestinations(refreshTokens);
    }

    @Override
    @Transactional
    public RefreshTokenDto save(RefreshTokenDto dto) {
        Assert.notNull(dto, "The 'dto' must not be null!");
        var entity = modelMapper.convertToSource(dto);
        return modelMapper.convertToDestination(refreshTokenRepository.save(entity));
    }

    @Override
    @Transactional
    public Set<RefreshTokenDto> save(Collection<RefreshTokenDto> dtos) {
        Assert.notEmpty(dtos, "The 'dtos' must not be empty or null!");
        var entities = modelMapper.convertToSources(dtos);
        return modelMapper.convertToDestinations(refreshTokenRepository.saveAll(entities));
    }

    @Override
    @Transactional
    public void deleteAll(Collection<RefreshTokenDto> dtos) {
        Assert.notNull(dtos, "The 'dtos' must not be null!");
        if (dtos.isEmpty()) {
            LOGGER.info("A collection of refresh tokens is empty. Do nothing. Return.");
            return;
        }
        var entities =
                dtos.stream()
                        .map(modelMapper::convertToSource)
                        .collect(Collectors.toSet());
        refreshTokenRepository.deleteAll(entities);
    }
}