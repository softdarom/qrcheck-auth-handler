package ru.softdarom.qrcheck.auth.handler.rest.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.BaseResponse;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j(topic = "AUTH-HANDLER-EXCEPTION-HANDLER")
public class AuthHandlerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<BaseResponse> notFound(NotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(NOT_FOUND).body(new BaseResponse(e.getMessage()));
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<BaseResponse> unauthorizedException(FeignException.Unauthorized e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(UNAUTHORIZED).body(new BaseResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<BaseResponse> unknown(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new BaseResponse(e.getMessage()));
    }

}