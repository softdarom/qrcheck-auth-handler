package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.dao.repository.RoleRepository;
import ru.softdarom.qrcheck.auth.handler.dao.repository.UserRepository;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserTokenInfoDto;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userDto;

@SpringIntegrationTest
@Sql(scripts = "classpath:sql/access/UserAccessService/fill.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/clear.sql", config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD)
@DisplayName("UserAccessService Spring Integration Test")
class UserAccessServiceTest {

    private static final Integer DEFAULT_SIZE_REFRESH_TOKENS_BY_USER = 4;
    private static final Integer DEFAULT_SIZE_ROLES_BY_USER = 1;
    private static final Integer DEFAULT_SIZE_TOKEN_INFO_BY_USER = 4;
    private static final Set<Long> DEFAULT_EXTERNAL_USER_IDS = Set.of(1L, 2L, 3L);

    @Autowired
    private UserAccessService userAccessService;

    //  -----------------------   successful tests   -------------------------

    // findByExternalUserId()

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

    // findByExternalUserIds()

    @Test
    @DisplayName("findByExternalUserIds(): returns a set of UserDto")
    void successfulFindByExternalUserIds() {
        var actual = assertDoesNotThrow(() -> userAccessService.findByExternalUserIds(DEFAULT_EXTERNAL_USER_IDS));
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        });
    }

    @Test
    @DisplayName("findByExternalUserIds(): returns an empty set when a collection of external user ids is empty")
    void successfulFindByExternalUserIdsEmptyExternalUserIds() {
        var actual = assertDoesNotThrow(() -> userAccessService.findByExternalUserIds(Set.of()));
        assertAll(() -> {
            assertNotNull(actual);
            assertTrue(actual.isEmpty());
        });
    }

    // save()

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

    @ParameterizedTest
    @EnumSource(RoleType.class)
    @DisplayName("changeRole(): returns a user with changed role")
    void successfulChangeRole(RoleType role) {
        var userExternalId = 3L;
        var actual = assertDoesNotThrow(() -> userAccessService.changeRole(userExternalId, role));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(role, actual.getRoles().stream().findAny().orElseThrow().getName());
        });
    }

    //  -----------------------   failure tests   -------------------------

    // findByExternalUserId()

    @Test
    @DisplayName("findByExternalUserId(): throws IllegalArgumentException when an external user id is null")
    void failureFindByExternalUserIdNullExternalUserId() {
        assertThrows(IllegalArgumentException.class, () -> userAccessService.findByExternalUserId(null));
    }

    // findByExternalUserIds()

    @Test
    @DisplayName("findByExternalUserIds(): throws IllegalArgumentException when a collection of external user ids is null")
    void failureFindByExternalUserIdsNullExternalUserIds() {
        assertThrows(IllegalArgumentException.class, () -> userAccessService.findByExternalUserIds(null));
    }

    // save()

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
    @DisplayName("save(): throws IllegalArgumentException when role is null")
    void failureChangeRoleNullRole() {
        var randomId = generateLong();
        assertThrows(IllegalArgumentException.class, () -> userAccessService.changeRole(randomId, null));
    }

    // changeRole()

    @ParameterizedTest
    @EnumSource(RoleType.class)
    @DisplayName("changeRole(): throws NotFoundException when a role not existed")
    void failureChangeRoleNotFoundRole(RoleType role) {
        var repositoryReal = (RoleRepository) ReflectionTestUtils.getField(userAccessService, "roleRepository");
        var repositoryMock = Mockito.mock(RoleRepository.class);
        ReflectionTestUtils.setField(userAccessService, "roleRepository", repositoryMock);

        var randomId = generateLong();
        when(repositoryMock.findByName(any())).thenReturn(Optional.empty());
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> userAccessService.changeRole(randomId, role));
            verify(repositoryMock, only()).findByName(any());
        });

        Mockito.reset(repositoryMock);
        ReflectionTestUtils.setField(userAccessService, "roleRepository", repositoryReal);
    }

    @ParameterizedTest
    @EnumSource(RoleType.class)
    @DisplayName("changeRole(): throws NotFoundException when a user not existed")
    void failureChangeRoleNotFoundUser(RoleType role) {
        var repositoryReal = (UserRepository) ReflectionTestUtils.getField(userAccessService, "userRepository");
        var repositoryMock = Mockito.mock(UserRepository.class);
        ReflectionTestUtils.setField(userAccessService, "userRepository", repositoryMock);

        var randomId = generateLong();
        when(repositoryMock.findByExternalUserId(any())).thenReturn(Optional.empty());
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> userAccessService.changeRole(randomId, role));
            verify(repositoryMock, only()).findByExternalUserId(any());
        });

        Mockito.reset(repositoryMock);
        ReflectionTestUtils.setField(userAccessService, "userRepository", repositoryReal);
    }
}