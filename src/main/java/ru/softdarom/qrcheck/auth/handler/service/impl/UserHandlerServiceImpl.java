package ru.softdarom.qrcheck.auth.handler.service.impl;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.client.UserHandlerClient;
import ru.softdarom.qrcheck.auth.handler.dao.access.ApiKeyAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;
import ru.softdarom.qrcheck.auth.handler.service.UserHandlerService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j(topic = "SERVICE")
public class UserHandlerServiceImpl implements UserHandlerService {

    @Value("${spring.application.name}")
    private String serviceName;
    private final UserHandlerClient userHandlerClient;
    private final ApiKeyAccessService accessService;

    @Autowired
    UserHandlerServiceImpl(UserHandlerClient userHandlerClient, ApiKeyAccessService accessService) {
        this.userHandlerClient = userHandlerClient;
        this.accessService = accessService;
    }

    @Override
    public boolean isExistedUser(Long externalUserId) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not null!");
        try {
            return Optional.ofNullable(userHandlerClient.get(getApiKey(), externalUserId).getBody()).isPresent();
        } catch (FeignException.NotFound e) {
            LOGGER.warn("A user (id: {}) not found! Return false.", externalUserId);
            return false;
        }
    }

    @Override
    public Optional<Long> saveUser(ProviderUserDto request) {
        Assert.notNull(request, "The 'request' must not null!");
        LOGGER.info("A user (email: {}) will be saved.", request.getEmail());
        return Optional.ofNullable(userHandlerClient.save(getApiKey(), request).getBody()).map(ProviderUserDto::getId);
    }

    private UUID getApiKey() {
        return accessService.find(serviceName, ApiKeyType.OUTGOING)
                .stream().findAny()
                .orElseThrow(() -> new NotFoundException("Not found outgoing api key for a service: " + serviceName));
    }
}