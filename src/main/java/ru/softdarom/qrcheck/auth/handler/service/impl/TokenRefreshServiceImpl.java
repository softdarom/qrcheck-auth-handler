package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.builder.AccessTokenDtoBuilder;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.service.OAuth2ProviderService;
import ru.softdarom.qrcheck.auth.handler.service.TokenRefreshService;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j(topic = "SERVICE")
public class TokenRefreshServiceImpl implements TokenRefreshService {

    private final AccessTokenAccessService accessTokenAccessService;
    private final RefreshTokenAccessService refreshTokenAccessService;
    private final OAuth2ProviderService oAuth2ProviderService;

    @Autowired
    TokenRefreshServiceImpl(AccessTokenAccessService accessTokenAccessService,
                            RefreshTokenAccessService refreshTokenAccessService,
                            OAuth2ProviderService oAuth2ProviderService) {
        this.accessTokenAccessService = accessTokenAccessService;
        this.refreshTokenAccessService = refreshTokenAccessService;
        this.oAuth2ProviderService = oAuth2ProviderService;
    }

    @Override
    @Transactional
    public RefreshTokenResponse refresh(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        LOGGER.info("Refreshing an access token: {}", accessToken);
        var foundAccessToken =
                accessTokenAccessService.findByToken(accessToken)
                        .orElseThrow(() -> new NotFoundException("Access token not found by " + accessToken));
        var refreshToken = getRefreshToken(foundAccessToken);
        var newOAuth2AccessToken = oAuth2ProviderService.refreshToken(refreshToken.getToken(), foundAccessToken.getProvider());
        var newAccessToken = new AccessTokenDtoBuilder.ByOAuth2AccessToken(newOAuth2AccessToken, refreshToken).build();
        disableOldAccessTokens(refreshToken);
        var savedAccessToken = accessTokenAccessService.save(newAccessToken);
        return new RefreshTokenResponse(savedAccessToken.getToken());
    }

    private RefreshTokenDto getRefreshToken(AccessTokenDto accessToken) {
        var userId = accessToken.getRefreshToken().getUser().getId();
        return refreshTokenAccessService.find(userId, accessToken.getProvider())
                .stream()
                .filter(it -> Objects.equals(it.getActive(), ActiveType.ENABLED))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Active refresh token not found for an access token: " + accessToken));
    }

    @Override
    public void disableOldAccessTokens(Set<RefreshTokenDto> refreshTokens, ProviderType provider) {
        refreshTokens.forEach(it -> disableOldAccessTokens(it, provider));
    }

    @Override
    public void disableOldAccessTokens(RefreshTokenDto refreshToken, ProviderType provider) {
        Assert.notNull(refreshToken, "The 'refreshToken' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        refreshToken.getAccessTokens()
                .stream()
                .filter(it -> Objects.equals(it.getProvider(), provider))
                .forEach(it -> it.setActive(ActiveType.DISABLED));
    }

    @Override
    public void disableOldRefreshToken(Set<RefreshTokenDto> refreshTokens, ProviderType provider) {
        Assert.notNull(refreshTokens, "The 'refreshToken' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        refreshTokens
                .stream()
                .filter(it -> Objects.equals(it.getProvider(), provider))
                .forEach(it -> it.setActive(ActiveType.DISABLED));
    }

    private void disableOldAccessTokens(RefreshTokenDto refreshToken) {
        disableOldAccessTokens(refreshToken, refreshToken.getProvider());
        refreshTokenAccessService.save(refreshToken);
    }
}