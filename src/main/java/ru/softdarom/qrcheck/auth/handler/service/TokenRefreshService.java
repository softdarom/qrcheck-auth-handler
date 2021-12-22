package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;

import java.util.Set;

public interface TokenRefreshService {

    RefreshTokenResponse refresh(String accessToken);

    void disableOldAccessTokens(Set<RefreshTokenDto> refreshTokens, ProviderType provider);

    void disableOldAccessTokens(RefreshTokenDto refreshToken, ProviderType provider);

    void disableOldRefreshToken(Set<RefreshTokenDto> refreshTokens, ProviderType provider);
}