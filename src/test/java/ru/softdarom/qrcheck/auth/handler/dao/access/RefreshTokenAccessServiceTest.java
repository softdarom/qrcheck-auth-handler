package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.refreshTokenDto;

@SpringIntegrationTest
@Sql(scripts = "classpath:sql/access/RefreshTokenAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("RefreshTokenAccessService Spring Integration Test")
class RefreshTokenAccessServiceTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Integer DEFAULT_SIZE_REFRESH_TOKEN_BY_USER_AND_PROVIDER = 1;

    @Autowired
    private RefreshTokenAccessService refreshTokenAccessService;

    @Autowired
    private UserAccessService userAccessService;

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

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("save(): returns a saved dto")
    void successfulSaveRefreshToken(ProviderType provider) {
        var user = userAccessService.findByExternalUserId(DEFAULT_USER_ID);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setUser(user.orElseThrow());
        var actual = assertDoesNotThrow(() -> refreshTokenAccessService.save(refreshToken));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(provider, actual.getProvider());
            assertEquals(ActiveType.ENABLED, actual.getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("save(): returns a saved dto")
    void successfulSaveRefreshTokens(ProviderType provider) {
        var user = userAccessService.findByExternalUserId(DEFAULT_USER_ID);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setUser(user.orElseThrow());
        var refreshTokens = Set.of(refreshToken);
        var result = assertDoesNotThrow(() -> refreshTokenAccessService.save(refreshTokens));
        assertAll(() -> {
            assertNotNull(result);
            var actual = result.stream().findAny().orElseThrow();
            assertNotNull(actual.getId());
            assertEquals(provider, actual.getProvider());
            assertEquals(ActiveType.ENABLED, actual.getActive());
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

    @Test
    @DisplayName("save(refreshToken): throws IllegalArgumentException when a dto is null")
    void failureSaveNullDto() {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenAccessService.save((RefreshTokenDto) null));
    }

    @Test
    @DisplayName("save(refreshTokens): throws IllegalArgumentException when a collection of dto is null")
    void failureSaveAllNullDtos() {
        assertThrows(IllegalArgumentException.class, () -> refreshTokenAccessService.save((Collection<RefreshTokenDto>) null));
    }
}