package ru.softdarom.qrcheck.auth.handler.dao.access.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.ApiKeyAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.entity.ApiKeyEntity;
import ru.softdarom.qrcheck.auth.handler.dao.entity.MicroserviceEntity;
import ru.softdarom.qrcheck.auth.handler.dao.repository.ApiKeyRepository;
import ru.softdarom.qrcheck.auth.handler.dao.repository.MicroserviceRepository;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.ApiKeyDtoMapper;
import ru.softdarom.qrcheck.auth.handler.mapper.impl.MicroserviceDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "ACCESS-SERVICE")
public class ApiKeyAccessServiceImpl implements ApiKeyAccessService {

    private static final String NOT_EMPTY_ARG_MESSAGE = "The 'serviceName' must not be null or empty!";
    private static final String NOT_NULL_ARG_MESSAGE = "The 'dto' must not be null!";

    private final MicroserviceRepository microserviceRepository;
    private final ApiKeyRepository apiKeyRepository;

    private final MicroserviceDtoMapper microserviceMapper;

    @Autowired
    ApiKeyAccessServiceImpl(MicroserviceRepository microserviceRepository,
                            ApiKeyRepository apiKeyRepository,
                            MicroserviceDtoMapper microserviceMapper) {
        this.microserviceRepository = microserviceRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.microserviceMapper = microserviceMapper;
    }

    @Override
    @Transactional
    public Set<UUID> find(String serviceName, ApiKeyType apiKeyType) {
        Assert.hasText(serviceName, NOT_EMPTY_ARG_MESSAGE);
        Assert.notNull(apiKeyType, "The 'apiKeyType' must not be null!");
        return apiKeyRepository.findAllByMicroserviceAndType(serviceName, apiKeyType)
                .stream()
                .map(ApiKeyEntity::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Optional<MicroserviceDto> find(String serviceName) {
        Assert.hasText(serviceName, NOT_EMPTY_ARG_MESSAGE);
        var entityOptional = microserviceRepository.findByName(serviceName);
        if (entityOptional.isEmpty()) {
            LOGGER.warn("Not found microservice by name [{}]", serviceName);
            return Optional.empty();
        }
        return Optional.of(microserviceMapper.convertToDestination(entityOptional.get()));
    }

    @Override
    @Transactional
    public MicroserviceDto save(MicroserviceDto dto) {
        Assert.notNull(dto, NOT_NULL_ARG_MESSAGE);
        var entity = microserviceMapper.convertToSource(dto);
        return microserviceMapper.convertToDestination(microserviceRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteApiKeys(String serviceName, Set<UUID> apiKeys) {
        Assert.hasText(serviceName, NOT_EMPTY_ARG_MESSAGE);
        Assert.notNull(apiKeys, "The 'apiKeys' must not be null!");
        var microserviceOptional = microserviceRepository.findByName(serviceName);
        if (microserviceOptional.isEmpty()) {
            LOGGER.warn("A microservice not found by name [{}]. Do nothing", serviceName);
            return;
        }
        var apiKeyIds = microserviceOptional
                .map(MicroserviceEntity::getApiKeys)
                .orElse(Set.of())
                .stream()
                .filter(it -> apiKeys.contains(it.getKey()))
                .map(ApiKeyEntity::getId)
                .collect(Collectors.toSet());

        if (apiKeyIds.isEmpty()) {
            LOGGER.warn("Api keys not found for deleting. Do nothing. Return.");
            return;
        }
        LOGGER.warn("Api key (ids: {}) will be removed!", apiKeyIds);
        apiKeyRepository.deleteAllById(apiKeyIds);
    }
}
