package ru.softdarom.qrcheck.auth.handler.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Validated
@ConfigurationProperties("springdoc.info")
public class OpenApiProperties {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String version;

    @NotEmpty
    private String licenceUrl;

    @NotEmpty
    private String ownerName;

    @NotEmpty
    private String ownerUrl;

    @NotEmpty
    private String ownerEmail;

}
