package ru.softdarom.qrcheck.auth.handler.test.generator;

import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateString;

public final class DtoGenerator {

    public static ProviderTokenDto providerTokenDto(ProviderType provider) {
        var dto = new ProviderTokenDto();
        dto.setSub(generateString());
        dto.setProvider(provider);
        dto.setAccessToken(accessToken());
        dto.setRefreshToken(refreshToken());
        return dto;
    }

    public static ProviderTokenDto.AccessToken accessToken() {
        var dto = new ProviderTokenDto.AccessToken();
        dto.setToken(generateString());
        dto.setIssuedAt(LocalDateTime.now());
        dto.setExpiresAt(LocalDateTime.now().plusHours(1L));
        return dto;
    }

    public static ProviderTokenDto.RefreshToken refreshToken() {
        var dto = new ProviderTokenDto.RefreshToken();
        dto.setToken(generateString());
        dto.setIssuedAt(LocalDateTime.now());
        return dto;
    }

    public static ProviderUserDto providerUserDto() {
        var dto = new ProviderUserDto();
        dto.setId(generateLong());
        dto.setFirstName(generateString());
        dto.setSecondName(generateString());
        dto.setEmail(generateString());
        dto.setPicture(generateString());
        return dto;
    }

    public static UserDto userDto(ProviderType provider) {
        var role = roleDto();
        var refreshToken = refreshTokenDto(provider);
        var accessToken = accessTokenDto(provider);
        accessToken.setRefreshToken(refreshToken);
        refreshToken.setAccessTokens(Set.of(accessToken));
        var refreshTokens = new HashSet<RefreshTokenDto>();
        refreshTokens.add(refreshToken);
        var userTokenInfo = userTokenInfoDto(provider);
        var user = UserDto.builder()
                .id(generateLong())
                .externalUserId(generateLong())
                .active(ActiveType.ENABLED)
                .refreshTokens(refreshTokens)
                .tokenInfo(Set.of(userTokenInfo))
                .roles(Set.of(role))
                .build();
        refreshToken.setUser(user);
        userTokenInfo.setUser(user);
        return user;
    }

    public static UserTokenInfoDto userTokenInfoDto(ProviderType provider) {
        return UserTokenInfoDto.builder()
                .id(generateLong())
                .sub(generateString())
                .provider(provider)
                .build();
    }

    public static RoleDto roleDto() {
        var dto = new RoleDto();
        dto.setId(generateLong());
        dto.setName(RoleType.USER);
        return dto;
    }

    public static RefreshTokenDto refreshTokenDto(ProviderType provider) {
        return RefreshTokenDto.builder()
                .id(generateLong())
                .token(generateString())
                .issued(LocalDateTime.now())
                .provider(provider)
                .active(ActiveType.ENABLED)
                .build();
    }

    public static AccessTokenDto accessTokenDto(ProviderType provider) {
        return AccessTokenDto.builder()
                .id(generateLong())
                .token(generateString())
                .provider(provider)
                .active(ActiveType.ENABLED)
                .issued(LocalDateTime.now())
                .expires(LocalDateTime.now().plusHours(1L))
                .build();
    }

    public static TokenUserInfoRequest tokenUserInfoRequest() {
        return new TokenUserInfoRequest(providerUserDto(), providerTokenDto(ProviderType.QRCHECK));
    }

    public static TokenUserInfoRequest tokenUserInfoRequest(ProviderType provider) {
        return new TokenUserInfoRequest(providerUserDto(), providerTokenDto(provider));
    }

    public static GoogleTokenInfoResponse googleTokenInfoResponse() {
        return new GoogleTokenInfoResponse(
                generateString(),
                generateString(),
                generateString(),
                "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid",
                generateString()
        );
    }

    public static GoogleAccessTokenResponse googleAccessTokenResponse() {
        return new GoogleAccessTokenResponse(generateString());
    }

    public static TokenUserInfoResponse tokenUserInfoResponse(TokenUserInfoRequest request) {
        return new TokenUserInfoResponse(request.getUser(), request.getToken());
    }

    public static AbstractOAuth2TokenInfoResponse.ExpiredTokenInfoResponse expiredTokenInfoResponse() {
        return new AbstractOAuth2TokenInfoResponse.ExpiredTokenInfoResponse();
    }

    public static AbstractOAuth2TokenInfoResponse.IncorrectTokenInfoResponse incorrectTokenInfoResponse() {
        return new AbstractOAuth2TokenInfoResponse.IncorrectTokenInfoResponse();
    }

    public static RefreshTokenResponse refreshTokenResponse() {
        return new RefreshTokenResponse(UUID.randomUUID().toString());
    }

    public static UserRoleResponse userRoleResponse() {
        return new UserRoleResponse(generateLong(), Set.of(RoleType.USER));
    }
}