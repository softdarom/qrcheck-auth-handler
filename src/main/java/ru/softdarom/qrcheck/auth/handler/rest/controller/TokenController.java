package ru.softdarom.qrcheck.auth.handler.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.TokenUserInfoRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.BaseResponse;
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

    @Operation(summary = "Сохранение auth-информации о user и token")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Auth-информация сохранена",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenUserInfoResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Отсутствует авторизация",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Неавторизованный запрос",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Неизвестная ошибка",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    )
            }
    )
    @Parameter(in = ParameterIn.HEADER, required = true, name = "X-ApiKey-Authorization", description = "ApiKey для доступа к API")
    @PostMapping("/info")
    public ResponseEntity<TokenUserInfoResponse> save(@Valid @RequestBody TokenUserInfoRequest request) {
        return ResponseEntity.ok(tokenService.saveOAuth2TokenInfo(request));
    }

    @Operation(summary = "Проверка валидности access token у провайдера")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access token прошел проверку",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AbstractOAuth2TokenInfoResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Отсутствует авторизация",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Неавторизованный запрос",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Access token не найден",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Неизвестная ошибка",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    )
            }
    )
    @Parameter(in = ParameterIn.HEADER, required = true, name = "X-ApiKey-Authorization", description = "ApiKey для доступа к API")
    @GetMapping("/verify")
    public ResponseEntity<AbstractOAuth2TokenInfoResponse> verify(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.verify(accessToken));
    }

    @Operation(summary = "Получение нового access token")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access token обновлен",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = AbstractOAuth2TokenInfoResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Access token не найден",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Неизвестная ошибка",
                            content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
                            }
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestParam String accessToken) {
        return ResponseEntity.ok(tokenService.refresh(accessToken));
    }
}