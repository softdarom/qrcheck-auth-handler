package ru.softdarom.qrcheck.auth.handler.config.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static ru.softdarom.qrcheck.auth.handler.config.swagger.OpenApiConfig.API_KEY_SECURITY_NAME;

@Operation(
        summary = "Получение api key'ев, которые разрешены для сервиса",
        security = @SecurityRequirement(name = API_KEY_SECURITY_NAME),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Api key'и получены",
                        content = {
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = UUID.class), uniqueItems = true))
                        }
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Отсутствует авторизация",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Api key'и не найдены",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Неизвестная ошибка",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                )
        }
)
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiGetIncoming {
}