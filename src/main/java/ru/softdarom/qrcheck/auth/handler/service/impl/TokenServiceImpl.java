package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.builder.UserDtoBuilder;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserTokenInfoDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.TokenUserInfoResponse;
import ru.softdarom.qrcheck.auth.handler.service.*;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j(topic = "SERVICE")
public class TokenServiceImpl implements TokenService {

    private final RoleAccessService roleAccessService;
    private final UserAccessService userAccessService;
    private final TokenVerifyService tokenVerifyService;
    private final TokenRefreshService tokenRefreshService;
    private final TokenDisabledService tokenDisabledService;
    private final UserHandlerService userHandlerService;

    @Autowired
    TokenServiceImpl(RoleAccessService roleAccessService,
                     UserAccessService userAccessService,
                     TokenVerifyService tokenVerifyService,
                     TokenRefreshService tokenRefreshService,
                     TokenDisabledService tokenDisabledService,
                     UserHandlerService userHandlerService) {
        this.roleAccessService = roleAccessService;
        this.userAccessService = userAccessService;
        this.tokenVerifyService = tokenVerifyService;
        this.tokenRefreshService = tokenRefreshService;
        this.tokenDisabledService = tokenDisabledService;
        this.userHandlerService = userHandlerService;
    }

    @Override
    public TokenUserInfoResponse saveOAuth2TokenInfo(TokenUserInfoRequest request) {
        Assert.notNull(request, "The 'request' must not be null!");
        var savedExternalUserId = userHandlerService.saveUser(request.getUser()).orElseThrow();
        saveOrUpdate(request, savedExternalUserId);
        return new TokenUserInfoResponse(request.getUser(), request.getToken());
    }

    @Override
    public AbstractOAuth2TokenInfoResponse verify(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        return tokenVerifyService.verify(accessToken);
    }

    @Override
    public RefreshTokenResponse refresh(String accessToken) {
        Assert.hasText(accessToken, "The 'accessToken' must not be null or empty!");
        return tokenRefreshService.refresh(accessToken);
    }

    private void saveOrUpdate(TokenUserInfoRequest request, Long savedExternalUserId) {
        request.getUser().setId(savedExternalUserId);
        var roles = roleAccessService.defaultRole();
        var dto = new UserDtoBuilder(request, Set.of(roles)).build();
        var optionalExistedUser = userAccessService.findByExternalUserId(savedExternalUserId);
        if (optionalExistedUser.isPresent()) {
            var existedUser = optionalExistedUser.get();
            disableOldTokens(existedUser.getRefreshTokens(), request.getToken().getProvider());
            existedUser.getRefreshTokens().addAll(dto.getRefreshTokens());
            addTokenInfoIfNew(existedUser, dto.getTokenInfo(), request.getToken().getProvider());
            userAccessService.save(existedUser);
        } else {
            userAccessService.save(dto);
        }
    }

    private void disableOldTokens(Set<RefreshTokenDto> refreshTokens, ProviderType provider) {
        tokenDisabledService.disableOldRefreshToken(refreshTokens, provider);
        tokenDisabledService.disableOldAccessTokens(refreshTokens);
    }

    private void addTokenInfoIfNew(UserDto existedUser, Set<UserTokenInfoDto> newTokenInfo, ProviderType provider) {
        var isNewTokenInfo = existedUser.getTokenInfo()
                .stream()
                .map(UserTokenInfoDto::getProvider)
                .noneMatch(it -> Objects.equals(it, provider));
        if (isNewTokenInfo) {
            existedUser.getTokenInfo().addAll(newTokenInfo);
        }
    }
}