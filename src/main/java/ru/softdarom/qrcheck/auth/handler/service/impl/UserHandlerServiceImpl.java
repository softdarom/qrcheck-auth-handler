package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.client.UserHandlerClient;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;
import ru.softdarom.qrcheck.auth.handler.service.UserHandlerService;
import ru.softdarom.security.oauth2.config.property.ApiKeyProperties;

import java.util.Optional;

@Service
@Slf4j(topic = "SERVICE")
public class UserHandlerServiceImpl implements UserHandlerService {

    private final UserHandlerClient userHandlerClient;
    private final ApiKeyProperties properties;

    @Autowired
    UserHandlerServiceImpl(UserHandlerClient userHandlerClient, ApiKeyProperties properties) {
        this.userHandlerClient = userHandlerClient;
        this.properties = properties;
    }

    @Override
    public Optional<Long> saveUser(ProviderUserDto request) {
        Assert.notNull(request, "The 'request' must not null!");
        LOGGER.info("Пользователь (id: {}) будет сохранен", request.getEmail());
        var apiKey = properties.getToken().getOutgoing();
        return Optional.ofNullable(userHandlerClient.save(apiKey, request).getBody()).map(ProviderUserDto::getId);
    }
}