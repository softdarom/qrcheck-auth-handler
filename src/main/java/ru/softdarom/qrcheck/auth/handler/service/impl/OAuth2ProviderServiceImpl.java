package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softdarom.qrcheck.auth.handler.client.ProviderClient;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2AccessTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.service.OAuth2ProviderService;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class OAuth2ProviderServiceImpl implements OAuth2ProviderService {

    private final Map<ProviderType, ProviderClient> providerToClient;

    @Autowired
    OAuth2ProviderServiceImpl(Set<ProviderClient> providerClients) {
        this.providerToClient = providerClients.stream().collect(Collectors.toMap(ProviderClient::getType, Function.identity()));
    }

    @Override
    public AbstractOAuth2TokenInfoResponse getTokenInfo(String accessToken, ProviderType provider) {
        LOGGER.info("Getting token info for an access token {} and a provider {}", accessToken, provider);
        checkClient(provider);
        return providerToClient.get(provider).tokenInfo(accessToken).getBody();
    }

    @Override
    public AbstractOAuth2AccessTokenResponse refreshToken(String refreshToken, ProviderType provider) {
        LOGGER.info("Refreshing an access token via {} external service", provider);
        checkClient(provider);
        return providerToClient.get(provider).refresh(refreshToken).getBody();
    }

    private void checkClient(ProviderType provider) {
        if (!providerToClient.containsKey(provider)) {
            throw new NotFoundException("A client for " + provider + " not found!");
        }
    }
}