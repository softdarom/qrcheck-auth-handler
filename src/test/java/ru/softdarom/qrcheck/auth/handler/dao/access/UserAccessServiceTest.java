package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserTokenInfoDto;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userDto;

@Sql(scripts = "classpath:sql/access/UserAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/access/UserAccessService/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("UserAccessService Spring Integration Test")
class UserAccessServiceTest extends AbstractIntegrationTest {

    private static final Integer DEFAULT_SIZE_REFRESH_TOKENS_BY_USER = 4;
    private static final Integer DEFAULT_SIZE_ROLES_BY_USER = 1;
    private static final Integer DEFAULT_SIZE_TOKEN_INFO_BY_USER = 4;

    @Autowired
    private UserAccessService userAccessService;

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    @DisplayName("findByExternalUserId(): returns UserDto")
    void successfulFindByExternalUserId(Long id) {
        var actual = assertDoesNotThrow(() -> userAccessService.findByExternalUserId(id));
        assertAll(() -> {
            assertTrue(actual.isPresent());
            assertEquals(id, actual.get().getId());
            assertEquals(DEFAULT_SIZE_REFRESH_TOKENS_BY_USER, actual.get().getRefreshTokens().size());
            assertEquals(DEFAULT_SIZE_ROLES_BY_USER, actual.get().getRoles().size());
            assertEquals(DEFAULT_SIZE_TOKEN_INFO_BY_USER, actual.get().getTokenInfo().size());
            assertEquals(ActiveType.ENABLED, actual.get().getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("save(): returns a saved UserDto")
    void successfulSave(ProviderType provider) {
        var dto = userDto(provider);
        dto.setId(null);
        dto.getRoles().forEach(it -> it.setId(1L));
        dto.getTokenInfo().forEach(it -> it.setId(null));
        dto.getRefreshTokens().forEach(refreshToken -> {
            refreshToken.setId(null);
            refreshToken.getAccessTokens().forEach(accessToken -> accessToken.setId(null));
        });
        var actual = assertDoesNotThrow(() -> userAccessService.save(dto));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getRoles().isEmpty());
            assertFalse(actual.getTokenInfo()
                    .stream()
                    .map(UserTokenInfoDto::getId)
                    .collect(Collectors.toSet()).isEmpty()
            );
            assertFalse(actual.getRefreshTokens()
                    .stream()
                    .map(RefreshTokenDto::getId)
                    .collect(Collectors.toSet())
                    .isEmpty()
            );
            assertFalse(actual.getRefreshTokens().stream()
                    .map(RefreshTokenDto::getAccessTokens)
                    .flatMap(Collection::stream)
                    .map(AccessTokenDto::getId)
                    .collect(Collectors.toSet())
                    .isEmpty()
            );
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("findByExternalUserId(): throws IllegalArgumentException when an external user id is null")
    void failureFindByExternalUserIdNullExternalUserId() {
        assertThrows(IllegalArgumentException.class, () -> userAccessService.findByExternalUserId(null));
    }

    @Test
    @DisplayName("save(): throws IllegalArgumentException when a dto is null")
    void failureSaveNullDto() {
        assertThrows(IllegalArgumentException.class, () -> userAccessService.save(null));
    }

    @Test
    @DisplayName("save(): throws IllegalArgumentException when externalUserId is null")
    void failureChangeRoleNullExternalUserId() {
        assertThrows(IllegalArgumentException.class, () -> userAccessService.changeRole(null, RoleType.USER));
    }

    @Test
    @DisplayName("save(): throws NotFoundException when role is null")
    void failureChangeRoleNullRole() {
        assertThrows(NotFoundException.class, () -> userAccessService.changeRole(generateLong(), null));
    }
}