package ru.softdarom.qrcheck.auth.handler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.GoogleTokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.GoogleAccessTokenResponse;

@FeignClient(name = "google-handler", url = "${outbound.feign.google-auth.host}")
public interface GoogleAuthClient extends ProviderClient {

    @Override
    @GetMapping("/tokens/info")
    ResponseEntity<GoogleTokenInfoResponse> tokenInfo(@RequestParam String accessToken);

    @Override
    @PostMapping("/tokens/refresh")
    ResponseEntity<GoogleAccessTokenResponse> refresh(@RequestParam String refreshToken);

    @Override
    default ProviderType getType() {
        return ProviderType.GOOGLE;
    }
}