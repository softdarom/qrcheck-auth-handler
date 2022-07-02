package ru.softdarom.qrcheck.auth.handler.rest.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;

import java.net.BindException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j(topic = "EXCEPTION-HANDLER")
public class AuthHandlerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(NotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<ErrorResponse> unauthorizedException(FeignException.Unauthorized e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    ResponseEntity<ErrorResponse> badRequest(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> unknown(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
    }

}