package ru.softdarom.qrcheck.auth.handler.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import ru.softdarom.qrcheck.auth.handler.config.property.ApiKeyProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
@Slf4j(topic = "AUTH-HANDLER-SECURITY")
public class ApiKeyAuthorizationConfig {

    @Bean
    ApiKeyAuthorizationFilter apiKeyAuthorizationFilter(ApiKeyProperties properties) {
        var filter = new ApiKeyAuthorizationFilter(properties.getName());
        filter.setAuthenticationManager(authentication -> {
            var credentials = Optional.ofNullable(authentication.getCredentials()).map(Object::toString).orElse(null);
            if (StringUtils.hasText(credentials) && properties.getToken().getIncoming().contains(credentials)) {
                LOGGER.debug("Authentication is successful by Api Key: '{}'", credentials);
                authentication.setAuthenticated(true);
                return authentication;
            } else {
                throw new BadCredentialsException("The API key was not an expected value!");
            }
        });
        return filter;
    }

    @Bean
    KeyApiAuthenticationProvider keyApiAuthenticationProvider() {
        return new KeyApiAuthenticationProvider();
    }

    static class ApiKeyAuthorizationFilter extends AbstractPreAuthenticatedProcessingFilter {

        private static final String NOT_AVAILABILITY = "N/A";

        private final String apiKeyHeaderName;

        public ApiKeyAuthorizationFilter(String apiKeyHeaderName) {
            this.apiKeyHeaderName = apiKeyHeaderName;
        }

        @Override
        protected String getPreAuthenticatedPrincipal(HttpServletRequest request) {
            return NOT_AVAILABILITY;
        }

        @Override
        protected String getPreAuthenticatedCredentials(HttpServletRequest request) {
            return Optional.ofNullable(request.getHeader(apiKeyHeaderName)).orElse(NOT_AVAILABILITY);
        }
    }

    static class KeyApiAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return authentication;
        }

        @Override
        public final boolean supports(Class<?> authentication) {
            return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}