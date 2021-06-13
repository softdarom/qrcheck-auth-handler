package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;

public interface TokenVerifyService {

    AbstractOAuth2TokenInfoResponse verify(String accessToken);

}