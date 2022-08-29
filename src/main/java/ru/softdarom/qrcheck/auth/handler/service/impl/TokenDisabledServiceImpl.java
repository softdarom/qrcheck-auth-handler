package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.service.TokenDisabledService;

import java.util.Collection;
import java.util.Objects;

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
        accessToken.setActive(ActiveType.DISABLED);
        accessTokenAccessService.save(accessToken);
    }

    @Override
    public void disableAccessTokens(RefreshTokenDto refreshToken) {
        Assert.notNull(refreshToken, "The 'refreshToken' must not be null!");
        refreshToken.getAccessTokens()
                .stream()
                .filter(it -> Objects.equals(it.getProvider(), refreshToken.getProvider()))
                .forEach(it -> it.setActive(ActiveType.DISABLED));
        refreshTokenAccessService.save(refreshToken);
    }

    @Override
    public void disableAccessTokens(Collection<RefreshTokenDto> refreshTokens) {
        Assert.notNull(refreshTokens, "The 'refreshTokens' must not be null!");
        refreshTokens.forEach(this::disableAccessTokens);
    }

    @Override
    public void disableRefreshToken(Collection<RefreshTokenDto> refreshTokens, ProviderType provider) {
        Assert.notNull(refreshTokens, "The 'refreshToken' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        refreshTokens
                .stream()
                .filter(it -> Objects.equals(it.getProvider(), provider))
                .forEach(it -> it.setActive(ActiveType.DISABLED));
        refreshTokenAccessService.save(refreshTokens);
    }

}
