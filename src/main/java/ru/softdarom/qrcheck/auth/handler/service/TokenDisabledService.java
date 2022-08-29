package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;

import java.util.Collection;

public interface TokenDisabledService {

    void disableAccessToken(AccessTokenDto accessToken);

    void disableAccessTokens(RefreshTokenDto refreshToken);

    void disableAccessTokens(Collection<RefreshTokenDto> refreshTokens);

    void disableRefreshTokens(Collection<RefreshTokenDto> refreshTokens, ProviderType provider);

}
