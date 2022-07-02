package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
@DisplayName("MicroserviceRepository Spring Integration Test")
class MicroserviceRepositoryTest {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private MicroserviceRepository repository;

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("findByName(): returns a microservice")
    void successfulFindByName() {
        var actual = assertDoesNotThrow(() -> repository.findByName(serviceName));
        assertAll(() -> {
            assertFalse(actual.isEmpty());
            assertEquals(serviceName, actual.get().getName());
        });
    }
}