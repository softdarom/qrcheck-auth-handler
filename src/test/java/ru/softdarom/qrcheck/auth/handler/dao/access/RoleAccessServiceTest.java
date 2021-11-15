package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@Sql(scripts = "classpath:sql/access/RoleAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/access/RoleAccessService/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RoleAccessService Spring Integration Test")
class RoleAccessServiceTest extends AbstractIntegrationTest {

    private static final Integer DEFAULT_SIZE_ROLES_FOR_USERS = 1;

    @Autowired
    private RoleAccessService roleAccessService;

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("find(): returns default role")
    void successfulFind() {
        var actual = assertDoesNotThrow(roleAccessService::defaultRole);
        assertNotNull(actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findByUserId(): returns roles by user id")
    void successfulFindByUserId(Long userId) {
        var actual = assertDoesNotThrow(() -> roleAccessService.findByUserId(userId));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES_FOR_USERS, actual.size());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findByExternalUserId(): returns roles by externalUserId")
    void successfulFindByExternalUserId(Long externalUserId) {
        var actual = assertDoesNotThrow(() -> roleAccessService.findByExternalUserId(externalUserId));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES_FOR_USERS, actual.size());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("findByUserId(): throws IllegalArgumentException when a user id is null")
    void failureFindByUserIdNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> roleAccessService.findByUserId(null));
    }

    @Test
    @DisplayName("findByExternalUserId(): throws IllegalArgumentException when a externalUserId is null")
    void failureFindByExternalUserIdNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> roleAccessService.findByExternalUserId(null));
    }
}