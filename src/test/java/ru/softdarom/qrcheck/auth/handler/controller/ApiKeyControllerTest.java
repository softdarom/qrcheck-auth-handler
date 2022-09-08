package ru.softdarom.qrcheck.auth.handler.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.IncomingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.OutgoingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.ErrorResponse;
import ru.softdarom.qrcheck.auth.handler.rest.controller.ApiKeyController;
import ru.softdarom.qrcheck.auth.handler.service.ApiKeyService;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

@SpringIntegrationTest
@DisplayName("ApiKeyController Spring Integration Test")
class ApiKeyControllerTest extends AbstractControllerTest {

    private static final String DEFAULT_SERVICE_NAME_PARAMS = "?serviceName=";
    private static final String API_KEYS_INCOMING = "/apiKeys/incoming";
    private static final String API_KEY_OUTGOING = "/apiKeys/outgoing";

    @Mock
    private ApiKeyService apiKeyServiceMock;

    @Autowired
    private ApiKeyController controller;

    @Value("${spring.application.name}")
    private String serviceName;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "apiKeyService", apiKeyServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(apiKeyServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    // getIncoming

    @Test
    @DisplayName("getIncoming(): returns 200 when incoming api keys found")
    void successfulGetIncoming() {
        var uri = API_KEYS_INCOMING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        var response = Set.of(UUID.randomUUID());
        when(apiKeyServiceMock.getIncoming(serviceName)).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> get(Set.class, header, uri));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(apiKeyServiceMock).getIncoming(serviceName);
        });
    }

    // getOutgoing

    @Test
    @DisplayName("getOutgoing(): returns 200 when outgoing api key found")
    void successfulGetOutgoing() {
        var uri = API_KEY_OUTGOING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        var response = UUID.randomUUID();
        when(apiKeyServiceMock.getOutgoing(serviceName)).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> get(UUID.class, header, uri));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.OK);
            verify(apiKeyServiceMock).getOutgoing(serviceName);
        });
    }

    // saveIncoming

    @Test
    @DisplayName("saveIncoming(): returns 201 when save new ingoing api keys")
    void successfulSaveIncoming() {
        var header = buildApiKeyHeader();
        var request = incomingApiKeyRequest();
        var response = incomingApiKeyResponse();
        when(apiKeyServiceMock.saveIncoming(request)).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.CREATED);
            verify(apiKeyServiceMock).saveIncoming(request);
        });
    }

    // saveOutgoing

    @Test
    @DisplayName("saveIncoming(): returns 201 when save new outgoing api key")
    void successfulSaveOutgoing() {
        var header = buildApiKeyHeader();
        var request = outgoingApiKeyRequest();
        var response = outgoingApiKeyResponse();
        when(apiKeyServiceMock.saveOutgoing(request)).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCallWithBody().accept(actual, HttpStatus.CREATED);
            verify(apiKeyServiceMock).saveOutgoing(request);
        });
    }

    // deleteIncoming

    @Test
    @DisplayName("deleteIncoming(): returns 202 when delete incoming api keys")
    void successfulDeleteIncoming() {
        var header = buildApiKeyHeader();
        var request = incomingApiKeyRequest();
        doNothing().when(apiKeyServiceMock).removeApiKeys(request.getServiceName(), request.getIncoming());
        var actual =
                assertDoesNotThrow(() -> delete(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.ACCEPTED);
            verify(apiKeyServiceMock).removeApiKeys(request.getServiceName(), request.getIncoming());
        });
    }

    // deleteOutgoing

    @Test
    @DisplayName("deleteOutgoing(): returns 202 when saved outgoing api key")
    void successfulDeleteOutgoing() {
        var header = buildApiKeyHeader();
        var request = outgoingApiKeyRequest();
        doNothing().when(apiKeyServiceMock).removeApiKeys(request.getServiceName(), Set.of(request.getOutgoing()));
        var actual =
                assertDoesNotThrow(() -> delete(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.ACCEPTED);
            verify(apiKeyServiceMock).removeApiKeys(request.getServiceName(), Set.of(request.getOutgoing()));
        });
    }

    //  -----------------------   failure tests   -------------------------

    // getIncoming()

    @Test
    @DisplayName("getIncoming(): returns 401 when not authentication")
    void failureGetIncomingUnauthorized() {
        var uri = API_KEYS_INCOMING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildNotAuthHeader();
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).getIncoming(anyString());
        });
    }

    @Test
    @DisplayName("getIncoming(): returns 404 when api keys not found")
    void failureGetIncomingNotFound() {
        var uri = API_KEYS_INCOMING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.getIncoming(anyString())).thenThrow(NotFoundException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(apiKeyServiceMock).getIncoming(anyString());
        });
    }

    @Test
    @DisplayName("getIncoming(): returns 500 when unknown exception")
    void failureGetIncomingUnknownException() {
        var uri = API_KEYS_INCOMING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.getIncoming(anyString())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).getIncoming(anyString());
        });
    }

    // getOutgoing()

    @Test
    @DisplayName("getOutgoing(): returns 401 when not authentication")
    void failureGetOutgoingUnauthorized() {
        var uri = API_KEY_OUTGOING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildNotAuthHeader();
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).getOutgoing(anyString());
        });
    }

    @Test
    @DisplayName("getOutgoing(): returns 404 when api key not found")
    void failureGetOutgoingNotFound() {
        var uri = API_KEY_OUTGOING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.getOutgoing(anyString())).thenThrow(NotFoundException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(apiKeyServiceMock).getOutgoing(anyString());
        });
    }

    @Test
    @DisplayName("getOutgoing(): returns 500 when unknown exception")
    void failureGetOutgoingUnknownException() {
        var uri = API_KEY_OUTGOING + DEFAULT_SERVICE_NAME_PARAMS + serviceName;
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.getOutgoing(anyString())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(ErrorResponse.class, header, uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).getOutgoing(anyString());
        });
    }

    // saveIncoming()

    @Test
    @DisplayName("saveIncoming(): returns 400 when incorrect request")
    void failureSaveIncomingBadRequest() {
        var request = new IncomingApiKeyRequest(null, null);
        var header = buildApiKeyHeader();
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.BAD_REQUEST);
            verify(apiKeyServiceMock, never()).saveIncoming(any());
        });
    }

    @Test
    @DisplayName("saveIncoming(): returns 401 when not authentication")
    void failureSaveIncomingUnauthorized() {
        var request = incomingApiKeyRequest();
        var header = buildNotAuthHeader();
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).saveIncoming(any());
        });
    }

    @Test
    @DisplayName("saveIncoming(): returns 500 when unknown exception")
    void failureSaveIncomingUnknownException() {
        var request = incomingApiKeyRequest();
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.saveIncoming(any())).thenThrow(RuntimeException.class);
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).saveIncoming(any());
        });
    }

    // saveOutgoing()

    @Test
    @DisplayName("saveOutgoing(): returns 400 when incorrect request")
    void failureSaveOutgoingBadRequest() {
        var request = new OutgoingApiKeyRequest(null, null);
        var header = buildApiKeyHeader();
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.BAD_REQUEST);
            verify(apiKeyServiceMock, never()).saveOutgoing(any());
        });
    }

    @Test
    @DisplayName("saveOutgoing(): returns 401 when not authentication")
    void failureSaveOutgoingUnauthorized() {
        var request = outgoingApiKeyRequest();
        var header = buildNotAuthHeader();
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).saveOutgoing(any());
        });
    }

    @Test
    @DisplayName("saveOutgoing(): returns 500 when unknown exception")
    void failureSaveOutgoingUnknownException() {
        var request = outgoingApiKeyRequest();
        var header = buildApiKeyHeader();
        when(apiKeyServiceMock.saveOutgoing(any())).thenThrow(RuntimeException.class);
        var actual =
                assertDoesNotThrow(() -> post(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).saveOutgoing(any());
        });
    }

    // deleteIncoming()

    @Test
    @DisplayName("deleteIncoming(): returns 400 when incorrect request")
    void failureDeleteIncomingBadRequest() {
        var request = new IncomingApiKeyRequest(null, null);
        var header = buildApiKeyHeader();
        var actual =
                assertDoesNotThrow(() -> delete(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.BAD_REQUEST);
            verify(apiKeyServiceMock, never()).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteIncoming(): returns 401 when not authentication")
    void failureDeleteIncomingUnauthorized() {
        var request = incomingApiKeyRequest();
        var header = buildNotAuthHeader();
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteIncoming(): returns 404 when microservice or api keys not found")
    void failureDeleteIncomingNotFound() {
        var request = incomingApiKeyRequest();
        var header = buildApiKeyHeader();
        doThrow(NotFoundException.class).when(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteIncoming(): returns 500 when unknown exception")
    void failureDeleteIncomingUnknownException() {
        var request = incomingApiKeyRequest();
        var header = buildApiKeyHeader();
        doThrow(RuntimeException.class).when(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEYS_INCOMING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        });
    }

    // deleteOutgoing()

    @Test
    @DisplayName("deleteOutgoing(): returns 400 when incorrect request")
    void failureDeleteOutgoingBadRequest() {
        var request = new OutgoingApiKeyRequest(null, null);
        var header = buildApiKeyHeader();
        var actual =
                assertDoesNotThrow(() -> delete(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.BAD_REQUEST);
            verify(apiKeyServiceMock, never()).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteOutgoing(): returns 401 when not authentication")
    void failureDeleteOutgoingUnauthorized() {
        var request = outgoingApiKeyRequest();
        var header = buildNotAuthHeader();
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(apiKeyServiceMock, never()).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteOutgoing(): returns 404 when microservice or api key not found")
    void failureDeleteOutgoingNotFound() {
        var request = outgoingApiKeyRequest();
        var header = buildApiKeyHeader();
        doThrow(NotFoundException.class).when(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("deleteOutgoing(): returns 500 when unknown exception")
    void failureDeleteOutgoingUnknownException() {
        var request = outgoingApiKeyRequest();
        var header = buildApiKeyHeader();
        doThrow(RuntimeException.class).when(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        var actual = assertDoesNotThrow(() -> delete(request, header, API_KEY_OUTGOING));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(apiKeyServiceMock).removeApiKeys(anyString(), anySet());
        });
    }
}