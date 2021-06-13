package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2AccessTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;

public interface OAuth2ProviderService {

    AbstractOAuth2TokenInfoResponse getTokenInfo(String accessToken, ProviderType provider);

    AbstractOAuth2AccessTokenResponse refreshToken(String refreshToken, ProviderType provider);
}