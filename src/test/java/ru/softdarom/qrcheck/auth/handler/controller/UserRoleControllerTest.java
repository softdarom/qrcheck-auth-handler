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
import ru.softdarom.qrcheck.auth.handler.rest.controller.UserRoleController;
import ru.softdarom.qrcheck.auth.handler.service.UserRoleService;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userRoleResponse;

@SpringIntegrationTest
@DisplayName("UserRoleController Spring Integration Test")
class UserRoleControllerTest extends AbstractControllerTest {

    private static final String URI_GET_USER_ROLES = "/users/1/roles";
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
    @DisplayName("getRoles(): returns 200 when roles found")
    void successfulGetRoles() {
        when(userRoleServiceMock.getRoles(any())).thenReturn(userRoleResponse());
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildApiKeyHeader(), URI_GET_USER_ROLES));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(userRoleServiceMock).getRoles(any());
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

    @Test
    @DisplayName("getRoles(): returns 401 when not authentication")
    void failureGetRolesUnauthorized() {
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildNotAuthHeader(), URI_GET_USER_ROLES));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(userRoleServiceMock, never()).getRoles(any());
        });
    }

    @Test
    @DisplayName("getRoles(): returns 500 when unknown error")
    void failureGetRolesUnknown() {
        when(userRoleServiceMock.getRoles(any())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, buildApiKeyHeader(), URI_GET_USER_ROLES));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(userRoleServiceMock).getRoles(any());
        });
    }

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