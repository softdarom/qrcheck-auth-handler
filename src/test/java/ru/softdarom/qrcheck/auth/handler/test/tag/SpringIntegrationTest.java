package ru.softdarom.qrcheck.auth.handler.test.tag;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.softdarom.qrcheck.auth.handler.AuthHandlerApplication;
import ru.softdarom.qrcheck.auth.handler.test.config.WireMockConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("spring-integration")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                AuthHandlerApplication.class,
                WireMockConfig.class
        }
)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration-test")
public @interface SpringIntegrationTest {
}