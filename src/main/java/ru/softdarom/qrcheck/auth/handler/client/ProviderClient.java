package ru.softdarom.qrcheck.auth.handler.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2AccessTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;

public interface ProviderClient {

    ResponseEntity<? extends AbstractOAuth2TokenInfoResponse> tokenInfo(@RequestParam String accessToken);

    ResponseEntity<? extends AbstractOAuth2AccessTokenResponse> refresh(@RequestParam String refreshToken);

    ProviderType getType();

}