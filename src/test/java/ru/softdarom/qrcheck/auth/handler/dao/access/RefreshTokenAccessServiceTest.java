package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@Sql(scripts = "classpath:sql/access/RefreshTokenAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/access/RefreshTokenAccessService/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RefreshTokenAccessService Spring Integration Test")
class RefreshTokenAccessServiceTest extends AbstractIntegrationTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Integer DEFAULT_SIZE_REFRESH_TOKEN_BY_USER_AND_PROVIDER = 1;

    @Autowired
    private RefreshTokenAccessService refreshTokenAccessService;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("find(): returns refresh tokens")
    void successfulFind(ProviderType provider) {
        var actual = assertDoesNotThrow(() -> refreshTokenAccessService.find(DEFAULT_USER_ID, provider));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_REFRESH_TOKEN_BY_USER_AND_PROVIDER, actual.size());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("find(): throws IllegalArgumentException when a user id is null")
    void failureFindNullUserId(ProviderType provider) {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenAccessService.find(null, provider));
    }

    @Test
    @DisplayName("find(): throws IllegalArgumentException when a provider is null")
    void failureFindNullProvider() {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenAccessService.find(0L, null));
    }
}