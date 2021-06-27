package ru.softdarom.qrcheck.auth.handler.config.security;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Generated
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String DEFAULT_TRACE_ID_HEADER_NAME = "X-B3-TraceId";
    private static final String DEFAULT_SPAN_ID_HEADER_NAME = "X-B3-SpanId";

    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    public DefaultAuthenticationEntryPoint(ObjectMapper objectMapper, Tracer tracer) {
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        var traceId = tracer.currentSpan().context().traceIdString();
        var spanId = tracer.currentSpan().context().spanIdString();
        response.setHeader(DEFAULT_TRACE_ID_HEADER_NAME, traceId);
        response.setHeader(DEFAULT_SPAN_ID_HEADER_NAME, spanId);
        response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(exception.getMessage())));
        MDC.put("TEST", traceId);
    }
}