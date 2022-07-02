package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@SpringIntegrationTest
@Sql(scripts = "classpath:sql/repository/RoleRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RoleRepository Spring Integration Test")
class RoleRepositoryTest {

    private static final Integer DEFAULT_SIZE_ROLES = 1;

    @Autowired
    private RoleRepository repository;

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("findAllByNameIn(): returns collection of roles")
    void successfulFindAllByNameIn() {
        var actual = assertDoesNotThrow(() -> repository.findByName(RoleType.USER));
        assertAll(() -> {
            assertFalse(actual.isEmpty());
            assertEquals(RoleType.USER, actual.get().getName());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findAllByUserId(): returns collection of roles by userId")
    void successfulFindAllByUserId(Long userId) {
        var actual = assertDoesNotThrow(() -> repository.findAllByUserId(userId));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES, actual.size());
            assertEquals(Set.of(RoleType.USER), actual.stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("findAllByExternalUserId(): returns collection of roles by externalUserId")
    void successfulFindAllByExternalUserId(Long externalUserId) {
        var actual = assertDoesNotThrow(() -> repository.findAllByExternalUserId(externalUserId));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_ROLES, actual.size());
            assertEquals(Set.of(RoleType.USER), actual.stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        });
    }
}