package ru.softdarom.qrcheck.auth.handler.service;

import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.client.UserHandlerClient;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.providerUserDto;

@SpringIntegrationTest
@DisplayName("UserHandlerService Spring Integration Test")
class UserHandlerServiceTest {

    @Mock
    private UserHandlerClient userHandlerClientMock;

    @Autowired
    private UserHandlerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "userHandlerClient", userHandlerClientMock);
    }

    @AfterEach
    void tearDown() {
        reset(userHandlerClientMock);
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("saveUser(): returns a saved external user id")
    void successfulSaveUser() {
        var response = providerUserDto();
        when(userHandlerClientMock.save(any(), any())).thenReturn(ResponseEntity.ok(response));
        assertAll(() -> {
            var actual = assertDoesNotThrow(() -> service.saveUser(providerUserDto()));
            assertTrue(actual.isPresent());
            assertEquals(response.getId(), actual.orElseThrow());
            verify(userHandlerClientMock).save(any(), any());
        });
    }

    @Test
    @DisplayName("isExistedUser(): returns true when a user is found")
    void successfulIsExistedUser() {
        var externalUserId = generateLong();
        var response = providerUserDto();
        when(userHandlerClientMock.get(any(), any())).thenReturn(ResponseEntity.ok(response));
        assertAll(() -> {
            var actual = assertDoesNotThrow(() -> service.isExistedUser(externalUserId));
            assertTrue(actual);
            verify(userHandlerClientMock).get(any(), any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("saveUser(): throws IllegalArgumentException when a request is null")
    void failureSaveUserNullRequest() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> service.saveUser(null));
            verify(userHandlerClientMock, never()).save(any(), any());
        });
    }

    @Test
    @DisplayName("isExistedUser(): returns false when a user isn't found")
    void failureIsExistedUser() {
        var externalUserId = generateLong();
        when(userHandlerClientMock.get(any(), any())).thenThrow(FeignException.NotFound.class);
        assertAll(() -> {
            var actual = assertDoesNotThrow(() -> service.isExistedUser(externalUserId));
            assertFalse(actual);
            verify(userHandlerClientMock).get(any(), any());
        });
    }

    @Test
    @DisplayName("isExistedUser(): throws IllegalArgumentException when a externalUserId is null")
    void failureIsExistedUserNullExternalUserId() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> service.isExistedUser(null));
            verify(userHandlerClientMock, never()).get(any(), any());
        });
    }
}