package ru.softdarom.qrcheck.auth.handler.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.softdarom.qrcheck.auth.handler.config.property.ApiKeyProperties;
import ru.softdarom.qrcheck.auth.handler.config.property.OpenApiProperties;

@Configuration
public class OpenApiConfig {

    public static final String API_KEY_SECURITY_NAME = "api-key";

    private static final String LICENCE = "Лицензия API";
    private static final String API_KEY_DESCRIPTION = "Аутентификация через ApiKey";

    private final OpenApiProperties properties;

    @Autowired
    OpenApiConfig(OpenApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    OpenAPI customOpenApi(Info info, Components components) {
        return new OpenAPI()
                .components(components)
                .info(info);
    }

    @Bean
    Components components(ApiKeyProperties apiKeyProperties) {
        return new Components()
                .addSecuritySchemes(API_KEY_SECURITY_NAME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(apiKeyProperties.getHeaderName())
                                .description(API_KEY_DESCRIPTION)
                );
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