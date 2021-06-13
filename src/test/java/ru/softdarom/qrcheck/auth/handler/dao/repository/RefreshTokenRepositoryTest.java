package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
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

@Sql(scripts = "classpath:sql/repository/RefreshTokenRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/repository/RefreshTokenRepository/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RefreshTokenRepository Spring Integration Test")
class RefreshTokenRepositoryTest extends AbstractIntegrationTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Integer DEFAULT_SIZE_REFRESH_TOKEN_BY_USER_ID_PROVIDER = 1;

    @Autowired
    private RefreshTokenRepository repository;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("findAllByUserIdAndProvider(): returns a refresh token by userId and provider")
    void successfulFindAllByUserIdAndProvider(ProviderType provider) {
        var actual = assertDoesNotThrow(() -> repository.findAllByUserIdAndProvider(DEFAULT_USER_ID, provider));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(DEFAULT_SIZE_REFRESH_TOKEN_BY_USER_ID_PROVIDER, actual.size());
        });
    }
}