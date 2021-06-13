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
    private static final Integer DEFAULT_SIZE_DEFAULT_ROLES = 1;

    @Autowired
    private RoleAccessService roleAccessService;

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("find(): returns default roles")
    void successfulFind() {
        var actual = assertDoesNotThrow(roleAccessService::defaultRoles);
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_DEFAULT_ROLES, actual.size());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findByUserId(): returns roles")
    void successfulFindByUserId(Long id) {
        var actual = assertDoesNotThrow(() -> roleAccessService.findByUserId(id));
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
}