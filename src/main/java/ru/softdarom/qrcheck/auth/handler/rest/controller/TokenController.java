package ru.softdarom.qrcheck.auth.handler.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.RefreshTokenResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.TokenUserInfoResponse;
import ru.softdarom.qrcheck.auth.handler.service.TokenService;

import javax.validation.Valid;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/info")
    public ResponseEntity<TokenUserInfoResponse> save(@Valid @RequestBody TokenUserInfoRequest request) {
        return ResponseEntity.ok(tokenService.saveOAuth2TokenInfo(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<AbstractOAuth2TokenInfoResponse> verify(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.verify(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.refresh(accessToken));
    }
}