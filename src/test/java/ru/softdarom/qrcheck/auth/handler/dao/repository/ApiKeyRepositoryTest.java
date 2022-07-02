package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringIntegrationTest
@DisplayName("ApiKeyRepository Spring Integration Test")
class ApiKeyRepositoryTest {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private ApiKeyRepository repository;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ApiKeyType.class)
    @DisplayName("findAllByMicroserviceAndType(): returns a set of api keys")
    void successfulFindAllByMicroserviceAndType(ApiKeyType type) {
        var actual = assertDoesNotThrow(() -> repository.findAllByMicroserviceAndType(serviceName, type));
        assertFalse(actual.isEmpty());
    }
}