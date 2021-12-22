package ru.softdarom.qrcheck.auth.handler.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RoleDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.Set;

@Slf4j(topic = "BUILDER")
public final class UserDtoBuilder {

    private final Set<RoleDto> roles;
    private final Long userId;
    private final ProviderTokenDto providerToken;

    public UserDtoBuilder(TokenUserInfoRequest tokenUserInfo, Set<RoleDto> roles) {
        Assert.notNull(tokenUserInfo, "The 'tokenUserInfo' must not be null!");
        Assert.notEmpty(roles, "The 'roles' must not be null or empty!");
        this.userId = tokenUserInfo.getUser().getId();
        this.providerToken = tokenUserInfo.getToken();
        this.roles = roles;
    }

    public UserDto build() {
        LOGGER.debug("Building a UserDto by {}", JsonHelper.asJson(providerToken));
        var tokens = Set.of(buildTokens());
        return UserDto.builder()
                .externalUserId(userId)
                .roles(roles)
                .refreshTokens(tokens)
                .tokenInfo(Set.of(new UserTokenInfoDtoBuilder(providerToken).build()))
                .build();
    }

    private RefreshTokenDto buildTokens() {
        var refreshToken = new RefreshTokenDtoBuilder(providerToken.getRefreshToken(), providerToken.getProvider()).build();
        var accessToken = new AccessTokenDtoBuilder.ByProviderTokenDto(providerToken.getAccessToken(), providerToken.getProvider()).build();
        refreshToken.setAccessTokens(Set.of(accessToken));
        return refreshToken;
    }
}