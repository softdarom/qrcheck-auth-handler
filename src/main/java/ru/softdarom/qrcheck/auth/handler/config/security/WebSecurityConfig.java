package ru.softdarom.qrcheck.auth.handler.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AbstractPreAuthenticatedProcessingFilter apiKeyAuthorizationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    WebSecurityConfig(@Qualifier("qrCheckApiKeyAuthorizationFilter") AbstractPreAuthenticatedProcessingFilter apiKeyAuthorizationFilter,
                      @Qualifier("qrCheckAuthenticationProvider") AuthenticationProvider authenticationProvider,
                      @Qualifier("qrCheckAuthenticationEntryPoint") AuthenticationEntryPoint authenticationEntryPoint) {
        this.apiKeyAuthorizationFilter = apiKeyAuthorizationFilter;
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .formLogin()
                    .disable()
                .httpBasic()
                    .disable()
                .csrf()
                    .disable()
                .addFilter(apiKeyAuthorizationFilter)
                .authenticationProvider(authenticationProvider)
                .authorizeRequests(request ->
                        request
                                .antMatchers("/tokens/refresh").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling(handlerConfigurer -> handlerConfigurer.authenticationEntryPoint(authenticationEntryPoint));
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-api/**"
                )
                .antMatchers(
                        "/actuator/health/**",
                        "/actuator/prometheus/**"
                );
    }
}