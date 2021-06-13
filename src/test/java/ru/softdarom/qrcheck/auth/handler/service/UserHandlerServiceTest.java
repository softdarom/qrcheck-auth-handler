package ru.softdarom.qrcheck.auth.handler.service;

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
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.providerUserDto;

@DisplayName("UserHandlerService Spring Integration Test")
class UserHandlerServiceTest extends AbstractIntegrationTest {

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

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("saveUser(): throws IllegalArgumentException when a request is null")
    void failureSaveUserNullRequest() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> service.saveUser(null));
            verify(userHandlerClientMock, never()).save(any(), any());
        });
    }
}