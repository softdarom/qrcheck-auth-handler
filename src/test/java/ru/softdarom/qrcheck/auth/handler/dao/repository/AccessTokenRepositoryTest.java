package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.dao.entity.AccessTokenEntity;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;

@SpringIntegrationTest
@Sql(scripts = "classpath:sql/repository/AccessTokenRepository/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("AccessTokenRepository Spring Integration Test")
class AccessTokenRepositoryTest {

    @Autowired
    private AccessTokenRepository repository;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @ValueSource(strings = {
            "access_token_google_1",
            "access_token_qrcheck_2",
            "access_token_facebook_3",
            "access_token_vkontakte_4"
    })
    @DisplayName("findByToken(): returns an access token by token")
    void successfulFindByToken(String accessToken) {
        var actual = assertDoesNotThrow(() -> repository.findByToken(accessToken));
        assertAll(() -> {
            assertTrue(actual.isPresent());
            assertEquals(accessToken, actual.map(AccessTokenEntity::getToken).orElseThrow());
        });
    }
}