package ru.softdarom.qrcheck.auth.handler.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.roleDto;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userDto;

@SpringIntegrationTest
@DisplayName("UserRoleService Spring Integration Test")
class UserRoleServiceTest {

    @Mock
    private RoleAccessService roleAccessServiceMock;

    @Mock
    private UserAccessService userAccessServiceMock;

    @Autowired
    private UserRoleService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "roleAccessService", roleAccessServiceMock);
        ReflectionTestUtils.setField(service, "userAccessService", userAccessServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(roleAccessServiceMock, userAccessServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("getRoles(): returns roles")
    void successfulGetRoles() {
        when(roleAccessServiceMock.findByExternalUserId(any())).thenReturn(Set.of(roleDto()));
        var actual = assertDoesNotThrow(() -> service.getRoles(generateLong()));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getExternalUserId());
            assertNotNull(actual.getRoles());
            verify(roleAccessServiceMock).findByExternalUserId(any());
        });
    }

    @ParameterizedTest
    @EnumSource(RoleType.class)
    @DisplayName("changeRole(): returns roles")
    void successfulChangeRole(RoleType role) {
        when(userAccessServiceMock.changeRole(any(), any())).thenReturn(userDto(ProviderType.GOOGLE));
        var actual = assertDoesNotThrow(() -> service.changeRole(generateLong(), role));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getExternalUserId());
            assertNotNull(actual.getRoles());
            verify(userAccessServiceMock).changeRole(any(), any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("getRoles(): throws IllegalArgumentException when a externalUserId is null")
    void getRolesNullExternalUserId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRoles(null));
        verify(roleAccessServiceMock, never()).findByUserId(any());
    }

    @Test
    @DisplayName("getRoles(): throws IllegalArgumentException when a userId is null")
    void changeRoleNullExternalUserId() {
        assertThrows(IllegalArgumentException.class, () -> service.changeRole(null, RoleType.USER));
        verify(roleAccessServiceMock, never()).findByUserId(any());
    }

    @Test
    @DisplayName("getRoles(): throws IllegalArgumentException when a role is null")
    void changeRoleNullRole() {
        var userId = generateLong();
        assertThrows(IllegalArgumentException.class, () -> service.changeRole(userId, null));
        verify(roleAccessServiceMock, never()).findByUserId(any());
    }
}