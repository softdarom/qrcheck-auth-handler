package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.service.TokenDisabledService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class TokenDisabledServiceImpl implements TokenDisabledService {

    private final AccessTokenAccessService accessTokenAccessService;
    private final RefreshTokenAccessService refreshTokenAccessService;

    @Autowired
    TokenDisabledServiceImpl(AccessTokenAccessService accessTokenAccessService,
                             RefreshTokenAccessService refreshTokenAccessService) {
        this.accessTokenAccessService = accessTokenAccessService;
        this.refreshTokenAccessService = refreshTokenAccessService;
    }

    @Override
    public void disableAccessToken(AccessTokenDto accessToken) {
        Assert.notNull(accessToken, "The 'accessToken' must not be null!");
        accessTokenAccessService.delete(accessToken.getId());
    }

    @Override
    public void disableAccessTokens(RefreshTokenDto refreshToken) {
        Assert.notNull(refreshToken, "The 'refreshToken' must not be null!");
        accessTokenAccessService.deleteAll(refreshToken.getAccessTokens());
    }

    @Override
    public void disableAccessTokens(Collection<RefreshTokenDto> refreshTokens) {
        Assert.notEmpty(refreshTokens, "The 'refreshTokens' must not be empty or null!");
        var accessTokens = refreshTokens
                .stream()
                .map(RefreshTokenDto::getAccessTokens)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        accessTokenAccessService.deleteAll(accessTokens);
    }

    @Override
    public void disableRefreshTokens(Collection<RefreshTokenDto> refreshTokens, ProviderType provider) {
        Assert.notNull(refreshTokens, "The 'refreshToken' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        refreshTokenAccessService.deleteAll(refreshTokens);
    }

}
