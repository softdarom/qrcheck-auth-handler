package ru.softdarom.qrcheck.auth.handler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.softdarom.qrcheck.auth.handler.config.property.SwaggerProperties;

@Configuration
public class OpenApiConfig {

    private static final String LICENCE = "Лицензия API";

    private final SwaggerProperties properties;

    @Autowired
    OpenApiConfig(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    OpenAPI customOpenApi(Info info) {
        return new OpenAPI()
                .info(info);
    }

    @Bean
    Info info(License license, Contact contact) {
        return new Info()
                .title(properties.getTitle())
                .version(properties.getVersion())
                .description(properties.getDescription())
                .license(license)
                .contact(contact);
    }

    @Bean
    License license() {
        return new License()
                .name(LICENCE)
                .url(properties.getLicenceUrl());
    }

    @Bean
    Contact contact() {
        return new Contact()
                .name(properties.getOwnerName())
                .email(properties.getOwnerEmail())
                .url(properties.getOwnerUrl());
    }

}