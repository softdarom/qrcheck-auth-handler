package ru.softdarom.qrcheck.auth.handler.config.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static ru.softdarom.qrcheck.auth.handler.config.swagger.OpenApiConfig.API_KEY_SECURITY_NAME;

@Operation(
        summary = "Удаление входящих api key'ев",
        security = @SecurityRequirement(name = API_KEY_SECURITY_NAME),
        responses = {
                @ApiResponse(
                        responseCode = "202",
                        description = "Api key'и удалены",
                        content = {
                                @Content(mediaType = "application/json")
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
                        responseCode = "401",
                        description = "Отсутствует авторизация",
                        content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                        }
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Microservice или api key'и не найдены",
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
public @interface ApiDeleteIncoming {
}