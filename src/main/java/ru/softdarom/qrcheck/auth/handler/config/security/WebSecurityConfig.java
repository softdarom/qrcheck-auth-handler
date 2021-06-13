package ru.softdarom.qrcheck.auth.handler.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApiKeyAuthorizationConfig.ApiKeyAuthorizationFilter apiKeyAuthorizationFilter;
    private final ApiKeyAuthorizationConfig.KeyApiAuthenticationProvider keyApiAuthenticationProvider;

    @Autowired
    WebSecurityConfig(ApiKeyAuthorizationConfig.ApiKeyAuthorizationFilter apiKeyAuthorizationFilter,
                      ApiKeyAuthorizationConfig.KeyApiAuthenticationProvider keyApiAuthenticationProvider) {
        this.apiKeyAuthorizationFilter = apiKeyAuthorizationFilter;
        this.keyApiAuthenticationProvider = keyApiAuthenticationProvider;
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
                .authenticationProvider(keyApiAuthenticationProvider)
                .authorizeRequests(request ->
                        request.anyRequest().authenticated()
                )
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling(handlerConfigurer -> handlerConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/actuator/health/**",
                        "/actuator/prometheus/**"
                );
    }
}