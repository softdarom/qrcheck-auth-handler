package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.TokenUserInfoResponse;

public interface TokenService {

    TokenUserInfoResponse saveOAuth2TokenInfo(TokenUserInfoRequest request);

    AbstractOAuth2TokenInfoResponse verify(String accessToken);

    RefreshTokenResponse refresh(String accessToken);
}