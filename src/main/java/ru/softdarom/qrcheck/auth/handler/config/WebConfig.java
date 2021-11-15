package ru.softdarom.qrcheck.auth.handler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.softdarom.qrcheck.auth.handler.model.base.converter.SpringRoleTypeConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SpringRoleTypeConverter());
    }

}