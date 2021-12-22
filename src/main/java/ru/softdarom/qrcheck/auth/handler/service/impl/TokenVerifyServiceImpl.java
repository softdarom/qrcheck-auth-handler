package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RoleDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.service.OAuth2ProviderService;
import ru.softdarom.qrcheck.auth.handler.service.TokenVerifyService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class TokenVerifyServiceImpl implements TokenVerifyService {

    private final AccessTokenAccessService accessTokenAccessService;
    private final RefreshTokenAccessService refreshTokenAccessService;
    private final RoleAccessService roleAccessService;
    private final OAuth2ProviderService oAuth2ProviderService;

    @Autowired
    TokenVerifyServiceImpl(AccessTokenAccessService accessTokenAccessService,
                           RefreshTokenAccessService refreshTokenAccessService,
                           RoleAccessService roleAccessService,
                           OAuth2ProviderService oAuth2ProviderService) {
        this.accessTokenAccessService = accessTokenAccessService;
        this.refreshTokenAccessService = refreshTokenAccessService;
        this.roleAccessService = roleAccessService;
        this.oAuth2ProviderService = oAuth2ProviderService;
    }

    @Override
    public AbstractOAuth2TokenInfoResponse verify(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        LOGGER.info("Checking an access token: {}", accessToken);
        var optionalFoundAccessToken = accessTokenAccessService.findByToken(accessToken);
        if (optionalFoundAccessToken.isEmpty()) {
            LOGGER.warn("A token not found!");
            return AbstractOAuth2TokenInfoResponse.incorrectToken();
        }
        var foundAccessToken = optionalFoundAccessToken.get();

        var notExpired = isExpiredAccessToken(foundAccessToken);
        var accessTokenActive = isActiveAccessToken(foundAccessToken);
        var refreshTokenActive = isActiveRefreshToken(foundAccessToken);

        LOGGER.debug("Expired: {}", notExpired);
        LOGGER.debug("Access token active: {}", accessTokenActive);
        LOGGER.debug("Refresh token active: {}", refreshTokenActive);

        if (notExpired && accessTokenActive && refreshTokenActive) {
            var tokenInfo = oAuth2ProviderService.getTokenInfo(accessToken, foundAccessToken.getProvider());
            tokenInfo.addScopes(getRoles(foundAccessToken));
            tokenInfo.setUserId(foundAccessToken.getRefreshToken().getUser().getId());
            return tokenInfo;
        } else {
            LOGGER.info("A token is not valid.");
            return AbstractOAuth2TokenInfoResponse.expiredToken();
        }
    }

    private boolean isExpiredAccessToken(AccessTokenDto accessToken) {
        return accessToken.getExpires().isAfter(LocalDateTime.now());
    }

    private boolean isActiveAccessToken(AccessTokenDto accessToken) {
        return accessToken.getActive().isActive();
    }

    private boolean isActiveRefreshToken(AccessTokenDto accessToken) {
        var userId = accessToken.getRefreshToken().getUser().getId();
        return refreshTokenAccessService.find(userId, accessToken.getProvider())
                .stream()
                .anyMatch(it -> Objects.equals(it.getActive(), ActiveType.ENABLED));
    }

    private Set<String> getRoles(AccessTokenDto accessToken) {
        var userId = accessToken.getRefreshToken().getUser().getId();
        return roleAccessService.findByUserId(userId)
                .stream()
                .map(RoleDto::getName)
                .map(RoleType::getRole)
                .collect(Collectors.toSet());
    }
}