package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.accessTokenDto;

@Sql(scripts = "classpath:sql/access/AccessTokenAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/access/AccessTokenAccessService/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("AccessTokenAccessService Spring Integration Test")
class AccessTokenAccessServiceTest extends AbstractIntegrationTest {

    private static final Long DEFAULT_USER_ID = 1L;

    @Autowired
    private AccessTokenAccessService accessTokenAccessService;

    @Autowired
    private RefreshTokenAccessService refreshTokenAccessService;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @ValueSource(strings = {"access_token_google_1", "access_token_qrcheck_2", "access_token_facebook_3", "access_token_vkontakte_4"})
    @DisplayName("findByToken(): returns an access token dto")
    void successfulFindByToken(String accessToken) {
        var actual = assertDoesNotThrow(() -> accessTokenAccessService.findByToken(accessToken));
        assertAll(() -> {
            assertTrue(actual.isPresent());
            assertNotNull(actual.get().getId());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("save(): returns a saved dto")
    void successfulSave(ProviderType provider) {
        var refreshToken =
                refreshTokenAccessService.find(DEFAULT_USER_ID, provider)
                        .stream()
                        .findAny()
                        .orElseThrow();
        var accessToken = accessTokenDto(provider);
        accessToken.setId(null);
        accessToken.setRefreshToken(refreshToken);
        var actual = assertDoesNotThrow(() -> accessTokenAccessService.save(accessToken));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(provider, actual.getProvider());
            assertEquals(ActiveType.ENABLED, actual.getActive());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("disableAccessTokens(): disables active access tokens")
    void successfulFindById(Long id) {
        var actual = assertDoesNotThrow(() -> accessTokenAccessService.findById(id));
        assertAll(() -> {
            assertTrue(actual.isPresent());
            assertEquals(id, actual.get().getId());
            assertEquals(ActiveType.ENABLED, actual.get().getActive());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("findByToken(): throws IllegalArgumentException when an access token is null")
    void failureFindByTokenNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenAccessService.findByToken(null));
    }

    @Test
    @DisplayName("findByToken(): throws IllegalArgumentException when an access token is blank")
    void failureFindByTokenBlankAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenAccessService.findByToken(""));
    }

    @Test
    @DisplayName("findById(): throws IllegalArgumentException when an id is null")
    void failureFinByIdNullId() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenAccessService.findById(null));
    }

    @Test
    @DisplayName("save(): throws IllegalArgumentException when a dto is null")
    void failureSaveNullDto() {
        assertThrows(IllegalArgumentException.class, () -> accessTokenAccessService.save(null));
    }
}