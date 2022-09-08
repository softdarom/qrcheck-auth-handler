package ru.softdarom.qrcheck.auth.handler.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.UserRoleResponse;
import ru.softdarom.qrcheck.auth.handler.rest.controller.UserRoleController;
import ru.softdarom.qrcheck.auth.handler.service.UserRoleService;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userRoleResponse;

@SpringIntegrationTest
@DisplayName("UserRoleController Spring Integration Test")
class UserRoleControllerTest extends AbstractControllerTest {

    private static final String URI_GET_USER_ROLES_BY_ID = "/users/1/roles";
    private static final String URI_GET_USER_ROLES_BY_IDS = "/users/roles?userId=1&userId=2&userId=3";
    private static final String URI_CHANGE_USER_ROLE = "/users/1/roles/%s";

    @Mock
    private UserRoleService userRoleServiceMock;

    @Autowired
    private UserRoleController controller;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "userRoleService", userRoleServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(userRoleServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("getRoles(userId): returns 200 when roles found")
    void successfulGetRolesUserId() {
        when(userRoleServiceMock.getRoles(any(Long.class))).thenReturn(userRoleResponse());
        var actual = assertDoesNotThrow(() -> get(UserRoleResponse.class, buildApiKeyHeader(), URI_GET_USER_ROLES_BY_ID));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(userRoleServiceMock).getRoles(any(Long.class));
        });
    }

    @Test
    @DisplayName("getRoles(userIds): returns 200 when roles found")
    void successfulGetRolesUserIds() {
        when(userRoleServiceMock.getRoles(anyCollection())).thenReturn(Set.of(userRoleResponse()));
        var actual = assertDoesNotThrow(() -> get(Set.class, buildApiKeyHeader(), URI_GET_USER_ROLES_BY_IDS));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(userRoleServiceMock).getRoles(anyCollection());
        });
    }

    @ParameterizedTest
    @EnumSource(RoleType.class)
    @DisplayName("changeRole(): returns 200 when role was changed")
    void successfulChangeRole(RoleType role) {
        var uri = URI_CHANGE_USER_ROLE.replace("%s", role.getApiRole());
        when(userRoleServiceMock.changeRole(any(), any())).thenReturn(userRoleResponse());
        var actual = assertDoesNotThrow(() -> put(buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(userRoleServiceMock).changeRole(any(), any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    // getRoles(userId)

    @Test
    @DisplayName("getRoles(userId): returns 401 when not authentication")
    void failureGetRolesUserIdUnauthorized() {
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildNotAuthHeader(), URI_GET_USER_ROLES_BY_ID));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(userRoleServiceMock, never()).getRoles(any(Long.class));
        });
    }

    @Test
    @DisplayName("getRoles(userId): returns 500 when unknown error")
    void failureGetRolesUserIdUnknown() {
        when(userRoleServiceMock.getRoles(any(Long.class))).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildApiKeyHeader(), URI_GET_USER_ROLES_BY_ID));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(userRoleServiceMock).getRoles(any(Long.class));
        });
    }

    // getRoles(userIds)

    @Test
    @DisplayName("getRoles(userIds): returns 401 when not authentication")
    void failureGetRolesUserIdsUnauthorized() {
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildNotAuthHeader(), URI_GET_USER_ROLES_BY_IDS));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(userRoleServiceMock, never()).getRoles(anyCollection());
        });
    }

    @Test
    @DisplayName("getRoles(userIds): returns 500 when unknown error")
    void failureGetRolesUserIdsUnknown() {
        when(userRoleServiceMock.getRoles(anyCollection())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildApiKeyHeader(), URI_GET_USER_ROLES_BY_IDS));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(userRoleServiceMock).getRoles(anyCollection());
        });
    }

    // changeRole()

    @Test
    @DisplayName("changeRole(): returns 401 when not authentication")
    void failureChangeRoleUnauthorized() {
        var uri = URI_CHANGE_USER_ROLE.replace("%s", RoleType.USER.getApiRole());
        var actual = assertDoesNotThrow(() -> put(buildNotAuthHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(userRoleServiceMock, never()).changeRole(any(), any());
        });
    }

    @Test
    @DisplayName("changeRole(): returns 404 when a user not existed")
    void failureChangeRoleNotFound() {
        var uri = URI_CHANGE_USER_ROLE.replace("%s", RoleType.USER.getApiRole());
        when(userRoleServiceMock.changeRole(any(), any())).thenThrow(NotFoundException.class);
        var actual = assertDoesNotThrow(() -> put(buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(userRoleServiceMock).changeRole(any(), any());
        });
    }

    @Test
    @DisplayName("changeRole(): returns 500 when unknown error")
    void failureChangeRoleUnknown() {
        var uri = URI_CHANGE_USER_ROLE.replace("%s", RoleType.USER.getApiRole());
        when(userRoleServiceMock.changeRole(any(), any())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> put(buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(userRoleServiceMock).changeRole(any(), any());
        });
    }
}