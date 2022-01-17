package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;

public interface TokenRefreshService {

    RefreshTokenResponse refresh(String accessToken);
}