package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserEntity;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@Sql(scripts = "classpath:sql/repository/UserRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/repository/UserRepository/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("UserRepository Spring Integration Test")
class UserRepositoryTest extends AbstractIntegrationTest {


    @Autowired
    private UserRepository repository;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    @DisplayName("findByExternalUserId(): returns a user by externalUserId")
    void successfulFindByExternalUserId(Long externalUserId) {
        var actual = assertDoesNotThrow(() -> repository.findByExternalUserId(externalUserId));
        assertAll(() -> {
            assertTrue(actual.isPresent());
            assertEquals(externalUserId, actual.map(UserEntity::getExternalUserId).orElseThrow());
        });
    }
}