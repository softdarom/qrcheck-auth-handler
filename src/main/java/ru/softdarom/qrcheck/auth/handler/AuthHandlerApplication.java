package ru.softdarom.qrcheck.auth.handler;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.softdarom.qrcheck.auth.handler.config.property.ApiKeyProperties;
import ru.softdarom.qrcheck.auth.handler.config.property.LogbookProperties;
import ru.softdarom.qrcheck.auth.handler.config.property.OpenApiProperties;

@Generated
@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients
@EnableConfigurationProperties(
        {
                LogbookProperties.class,
                OpenApiProperties.class,
                ApiKeyProperties.class
        }
)
public class AuthHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthHandlerApplication.class, args);
    }

}
