package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserEntity;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@SpringIntegrationTest
@Sql(scripts = "classpath:sql/repository/UserRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("UserRepository Spring Integration Test")
class UserRepositoryTest {

    private static final Set<Long> DEFAULT_EXTERNAL_USER_IDS = Set.of(1L, 2L);

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

    @Test
    @DisplayName("findByExternalUserIds(): returns a users by externalUserIds")
    void successfulFindByExternalUserIds() {
        var actual = assertDoesNotThrow(() -> repository.findByExternalUserIds(DEFAULT_EXTERNAL_USER_IDS));
        assertAll(() -> {
            assertFalse(actual.isEmpty());
            assertEquals(DEFAULT_EXTERNAL_USER_IDS.size(), actual.size());
        });
    }
}