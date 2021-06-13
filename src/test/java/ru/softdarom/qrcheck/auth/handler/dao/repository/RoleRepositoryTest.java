package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@Sql(scripts = "classpath:sql/repository/RoleRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/repository/RoleRepository/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RoleRepository Spring Integration Test")
class RoleRepositoryTest extends AbstractIntegrationTest {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final Integer DEFAULT_SIZE_ROLES = 1;

    @Autowired
    private RoleRepository repository;

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("findAllByNameIn(): returns collection of roles")
    void successfulFindAllByNameIn() {
        var actual = assertDoesNotThrow(() -> repository.findAllByNameIn(Set.of(DEFAULT_ROLE)));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES, actual.size());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    @DisplayName("findAllByUserId(): returns collection of roles by userId")
    void successfulFindAllByUserId(Long userId) {
        var actual = assertDoesNotThrow(() -> repository.findAllByUserId(userId));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES, actual.size());
            assertEquals(Set.of(DEFAULT_ROLE), actual.stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        });
    }
}