package ru.softdarom.qrcheck.auth.handler.config.security;

import lombok.Generated;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Generated
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }

    @Override
    public final boolean supports(Class<?> authentication) {
        return isPreAuthenticatedAuthenticationToken(authentication);
    }

    private boolean isPreAuthenticatedAuthenticationToken(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}