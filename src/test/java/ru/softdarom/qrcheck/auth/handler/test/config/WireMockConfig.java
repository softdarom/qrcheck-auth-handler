package ru.softdarom.qrcheck.auth.handler.test.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireUserHandlerServiceMock(@Value("${outbound.feign.user-handler.port}") Integer port) {
        return new WireMockServer(port);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireGoogleAuthServiceMock(@Value("${outbound.feign.google-auth.port}") Integer port) {
        return new WireMockServer(port);
    }
}