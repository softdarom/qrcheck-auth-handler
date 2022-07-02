package ru.softdarom.qrcheck.auth.handler.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@ConfigurationProperties(value = "spring.security.qrcheck.api-key")
public class ApiKeyProperties {

    /**
     * <p> Имя header для аутентификации через api-key.
     * <p> Значение по-умолчанию "X-ApiKey-Authorization".
     **/
    private String headerName = "X-ApiKey-Authorization";

}