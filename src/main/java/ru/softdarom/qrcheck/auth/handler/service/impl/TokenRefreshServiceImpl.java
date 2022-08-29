package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.builder.AccessTokenDtoBuilder;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.service.OAuth2ProviderService;
import ru.softdarom.qrcheck.auth.handler.service.TokenDisabledService;
import ru.softdarom.qrcheck.auth.handler.service.TokenRefreshService;
import ru.softdarom.qrcheck.auth.handler.service.UserHandlerService;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class TokenRefreshServiceImpl implements TokenRefreshService {

    private final AccessTokenAccessService accessTokenAccessService;
    private final RefreshTokenAccessService refreshTokenAccessService;
    private final OAuth2ProviderService oAuth2ProviderService;
    private final TokenDisabledService tokenDisabledService;
    private final UserHandlerService userHandlerService;
    private final UserAccessService userAccessService;

    @Autowired
    TokenRefreshServiceImpl(AccessTokenAccessService accessTokenAccessService,
                            RefreshTokenAccessService refreshTokenAccessService,
                            OAuth2ProviderService oAuth2ProviderService,
                            TokenDisabledService tokenDisabledService,
                            UserHandlerService userHandlerService,
                            UserAccessService userAccessService) {
        this.accessTokenAccessService = accessTokenAccessService;
        this.refreshTokenAccessService = refreshTokenAccessService;
        this.oAuth2ProviderService = oAuth2ProviderService;
        this.tokenDisabledService = tokenDisabledService;
        this.userHandlerService = userHandlerService;
        this.userAccessService = userAccessService;
    }

    @Override
    public RefreshTokenResponse refresh(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        LOGGER.info("Refreshing an access token: {}", accessToken);
        var foundAccessToken =
                accessTokenAccessService.findByToken(accessToken)
                        .orElseThrow(() -> new NotFoundException("Access token not found by " + accessToken));
        checkExistUser(foundAccessToken);
        var refreshToken = getRefreshToken(foundAccessToken);
        var newOAuth2AccessToken =
                oAuth2ProviderService.refreshToken(refreshToken.getToken(), foundAccessToken.getProvider());
        var newAccessToken = new AccessTokenDtoBuilder.ByOAuth2AccessToken(newOAuth2AccessToken, refreshToken).build();
        tokenDisabledService.disableAccessTokens(refreshToken);
        var savedAccessToken = accessTokenAccessService.save(newAccessToken);
        return new RefreshTokenResponse(savedAccessToken.getToken());
    }

    private void checkExistUser(AccessTokenDto accessToken) {
        var externalUserId = accessToken.getRefreshToken().getUser().getExternalUserId();
        var exist = userHandlerService.isExistedUser(externalUserId);
        if (exist) {
            LOGGER.debug("A user is found. Do nothing");
        } else {
            LOGGER.warn("A user not found from an external system! Remove the user and the user's refreshTokens/accessTokens from the current service");
            userAccessService.findByExternalUserId(externalUserId).map(UserDto::getId).ifPresent(userAccessService::delete);
            throw new NotFoundException("A user (id: " + externalUserId + ") not existed! The user and the user's tokens have been removed!");
        }
    }

    private RefreshTokenDto getRefreshToken(AccessTokenDto accessToken) {
        var externalUserId = accessToken.getRefreshToken().getUser().getId();
        return refreshTokenAccessService.find(externalUserId, accessToken.getProvider())
                .stream()
                .filter(it -> Objects.equals(it.getActive(), ActiveType.ENABLED))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Active refresh token not found for an access token: " + accessToken));
    }
}