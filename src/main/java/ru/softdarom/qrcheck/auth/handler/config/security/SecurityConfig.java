package ru.softdarom.qrcheck.auth.handler.config.security;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;
import ru.softdarom.qrcheck.auth.handler.config.property.ApiKeyProperties;
import ru.softdarom.qrcheck.auth.handler.config.security.handler.DefaultAuthenticationEntryPoint;
import ru.softdarom.qrcheck.auth.handler.dao.access.ApiKeyAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Generated
@Configuration
@Slf4j(topic = "SECURITY")
public class SecurityConfig {

    @Bean(name = "defaultAuthenticationProvider")
    AuthenticationProvider defaultAuthenticationProvider() {
        return new DefaultAuthenticationProvider();
    }

    @Bean(name = "defaultAuthenticationEntryPoint")
    AuthenticationEntryPoint defaultAuthenticationEntryPoint(ObjectMapper objectMapper, Tracer tracer) {
        return new DefaultAuthenticationEntryPoint(objectMapper, tracer);
    }

    @Bean(name = "defaultApiKeyAuthorizationConfig")
    AbstractPreAuthenticatedProcessingFilter apiKeyExternalAuthorizationFilter(
            @Value("${spring.application.name}") String serviceName,
            ApiKeyAccessService accessService,
            ApiKeyProperties apiKeyProperties
    ) {
        var filter = new DefaultApiKeyAuthorization.ApiKeyAuthorizationFilter(apiKeyProperties.getHeaderName());
        filter.setAuthenticationManager(authentication -> {
            var credentials = Optional.ofNullable(authentication.getCredentials()).map(Object::toString).orElse(null);
            var incoming = accessService.find(serviceName, ApiKeyType.INCOMING).stream().map(UUID::toString).collect(Collectors.toSet());
            if (StringUtils.hasText(credentials) && incoming.contains(credentials)) {
                LOGGER.info("Success authentication by ApiKey: '{}'", credentials);
                return new DefaultApiKeyAuthorization.ApiKeyAuthentication(credentials);
            } else {
                throw new BadCredentialsException("The API key was not an expected value.");
            }
        });
        return filter;
    }
}
