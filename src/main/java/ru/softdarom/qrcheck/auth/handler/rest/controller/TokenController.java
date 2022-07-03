package ru.softdarom.qrcheck.auth.handler.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.ApVerifyToken;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.ApiRefreshToken;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.ApiSaveToken;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.TokenUserInfoResponse;
import ru.softdarom.qrcheck.auth.handler.service.TokenService;

import javax.validation.Valid;

@Tag(name = "Tokens", description = "Контроллер взаимодействия с access token'ами")
@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ApiSaveToken
    @PostMapping("/info")
    public ResponseEntity<TokenUserInfoResponse> save(@Valid @RequestBody TokenUserInfoRequest request) {
        return ResponseEntity.ok(tokenService.saveOAuth2TokenInfo(request));
    }

    @ApVerifyToken
    @GetMapping("/verify")
    public ResponseEntity<AbstractOAuth2TokenInfoResponse> verify(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.verify(accessToken));
    }

    @ApiRefreshToken
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.refresh(accessToken));
    }
}