package ru.softdarom.qrcheck.auth.handler.config.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

@Operation(
        summary = "Получение нового access token",
        responses = {
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
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                ),
                @ApiResponse(
                        responseCode = "500", description = "Неизвестная ошибка",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                )
        }
)
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRefreshToken {
}