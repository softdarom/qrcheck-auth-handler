package ru.softdarom.qrcheck.auth.handler.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.dao.access.ApiKeyAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateString;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

@SpringIntegrationTest
@DisplayName("ApiKeyService Spring Integration Test")
class ApiKeyServiceTest {

    @Mock
    private ApiKeyAccessService apiKeyAccessServiceMock;

    @Autowired
    private ApiKeyService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "apiKeyAccessService", apiKeyAccessServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(apiKeyAccessServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    //  getIncoming

    @Test
    @DisplayName("getIncoming(): returns incoming api keys")
    void successfulGetIncoming() {
        var serviceName = generateString();
        var expected = UUID.randomUUID();
        when(apiKeyAccessServiceMock.find(serviceName, ApiKeyType.INCOMING)).thenReturn(Set.of(expected));
        var actual = assertDoesNotThrow(() -> service.getIncoming(serviceName));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected, actual.stream().findAny().orElseThrow());
            verify(apiKeyAccessServiceMock, only()).find(serviceName, ApiKeyType.INCOMING);
        });
    }

    //  getOutgoing

    @Test
    @DisplayName("getOutgoing(): returns outgoing api key")
    void successfulGetOutgoing() {
        var serviceName = generateString();
        var expected = UUID.randomUUID();
        when(apiKeyAccessServiceMock.find(serviceName, ApiKeyType.OUTGOING)).thenReturn(Set.of(expected));
        var actual = assertDoesNotThrow(() -> service.getOutgoing(serviceName));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(expected, actual);
            verify(apiKeyAccessServiceMock, only()).find(serviceName, ApiKeyType.OUTGOING);
        });
    }

    //  saveIncoming

    @Test
    @DisplayName("saveIncoming(): returns a new microservice and new incoming api keys")
    void successfulSaveIncomingNewMicroservice() {
        var serviceName = generateString();
        var apiKeys = Set.of(apiKeyDto(ApiKeyType.INCOMING));
        var microservice = microserviceDto(serviceName, apiKeys);
        var request = incomingApiKeyRequest();
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.empty());
        when(apiKeyAccessServiceMock.save(any())).thenReturn(microservice);
        var actual = assertDoesNotThrow(() -> service.saveIncoming(request));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(microservice.getName(), actual.getServiceName());
            verify(apiKeyAccessServiceMock).find(anyString());
            verify(apiKeyAccessServiceMock).save(any());
        });
    }

    @Test
    @DisplayName("saveIncoming(): returns an old microservice and new incoming api keys")
    void successfulSaveIncomingExistedMicroservice() {
        var serviceName = generateString();
        var apiKeys = new HashSet<>(Set.of(apiKeyDto(ApiKeyType.INCOMING)));
        var microservice = microserviceDto(serviceName, apiKeys);
        var request = incomingApiKeyRequest();
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.of(microservice));
        when(apiKeyAccessServiceMock.save(any())).thenReturn(microservice);
        var actual = assertDoesNotThrow(() -> service.saveIncoming(request));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(microservice.getName(), actual.getServiceName());
            verify(apiKeyAccessServiceMock).find(anyString());
            verify(apiKeyAccessServiceMock).save(any());
        });
    }

    // saveOutgoing

    @Test
    @DisplayName("saveOutgoing(): returns a new microservice and a new outgoing api key")
    void successfulSaveOutgoingNewMicroservice() {
        var serviceName = generateString();
        var apiKeys = Set.of(apiKeyDto(ApiKeyType.OUTGOING));
        var microservice = microserviceDto(serviceName, apiKeys);
        var request = outgoingApiKeyRequest();
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.empty());
        when(apiKeyAccessServiceMock.save(any())).thenReturn(microservice);
        var actual = assertDoesNotThrow(() -> service.saveOutgoing(request));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(microservice.getName(), actual.getServiceName());
            verify(apiKeyAccessServiceMock).find(anyString());
            verify(apiKeyAccessServiceMock).save(any());
        });
    }

    @Test
    @DisplayName("saveOutgoing(): returns an old microservice and a new outgoing api key")
    void successfulSaveOutgoingExistedMicroservice() {
        var serviceName = generateString();
        var apiKeys = new HashSet<>(Set.of(apiKeyDto(ApiKeyType.OUTGOING)));
        var microservice = microserviceDto(serviceName, apiKeys);
        var request = outgoingApiKeyRequest();
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.of(microservice));
        when(apiKeyAccessServiceMock.save(any())).thenReturn(microservice);
        var actual = assertDoesNotThrow(() -> service.saveOutgoing(request));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(microservice.getName(), actual.getServiceName());
            verify(apiKeyAccessServiceMock).find(anyString());
            verify(apiKeyAccessServiceMock).save(any());
        });
    }

    //  removeApiKeys

    @Test
    @DisplayName("removeApiKeys(): deletes api keys")
    void successfulRemoveApiKeys() {
        var serviceName = generateString();
        var microservice = microserviceDto(serviceName);
        var apiKeys = microservice.getApiKeys().stream().map(ApiKeyDto::getKey).collect(Collectors.toSet());
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.of(microservice));
        doNothing().when(apiKeyAccessServiceMock).deleteApiKeys(anyString(), anySet());
        assertDoesNotThrow(() -> service.removeApiKeys(microservice.getName(), apiKeys));
        assertAll(() -> {
            verify(apiKeyAccessServiceMock).find(anyString());
            verify(apiKeyAccessServiceMock).deleteApiKeys(anyString(), anySet());
        });
    }

    //  -----------------------   failure tests   -------------------------

    // getIncoming

    @Test
    @DisplayName("getIncoming(): throws IllegalArgumentException when a serviceName is null")
    void failureGetIncomingNullServiceName() {
        assertThrows(IllegalArgumentException.class, () -> service.getIncoming(null));
    }

    @Test
    @DisplayName("getIncoming(): throws IllegalArgumentException when a serviceName is empty")
    void failureGetIncomingEmptyServiceName() {
        assertThrows(IllegalArgumentException.class, () -> service.getIncoming(""));
    }

    @Test
    @DisplayName("getIncoming(): throws NotFoundException when incoming api keys not found")
    void failureGetIncomingNotFound() {
        var serviceName = generateString();
        when(apiKeyAccessServiceMock.find(anyString(), any())).thenReturn(Set.of());
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> service.getIncoming(serviceName));
            verify(apiKeyAccessServiceMock, only()).find(anyString(), any());
        });
    }

    // getOutgoing

    @Test
    @DisplayName("getOutgoing(): throws IllegalArgumentException when a serviceName is null")
    void failureGetOutgoingNullServiceName() {
        assertThrows(IllegalArgumentException.class, () -> service.getOutgoing(null));
    }

    @Test
    @DisplayName("getOutgoing(): throws IllegalArgumentException when a serviceName is empty")
    void failureGetOutgoingEmptyServiceName() {
        assertThrows(IllegalArgumentException.class, () -> service.getOutgoing(""));
    }

    @Test
    @DisplayName("getIncoming(): throws NotFoundException when an outgoing api key not found")
    void failureGetOutgoingNotFound() {
        var serviceName = generateString();
        when(apiKeyAccessServiceMock.find(anyString(), any())).thenReturn(Set.of());
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> service.getOutgoing(serviceName));
            verify(apiKeyAccessServiceMock, only()).find(anyString(), any());
        });
    }

    // saveIncoming

    @Test
    @DisplayName("getOutgoing(): throws IllegalArgumentException when a request is null")
    void failureSaveIncomingNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.saveIncoming(null));
    }

    // saveOutgoing

    @Test
    @DisplayName("saveOutgoing(): throws IllegalArgumentException when a request is null")
    void failureSaveOutgoingNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.saveOutgoing(null));
    }

    // removeApiKeys

    @Test
    @DisplayName("removeApiKeys(): throws IllegalArgumentException when a serviceName is null")
    void failureRemoveApiKeysNullServiceName() {
        var apiKeys = Set.<UUID>of();
        assertThrows(IllegalArgumentException.class, () -> service.removeApiKeys(null, apiKeys));
    }

    @Test
    @DisplayName("removeApiKeys(): throws IllegalArgumentException when a serviceName is empty")
    void failureRemoveApiKeysEmptyServiceName() {
        var apiKeys = Set.<UUID>of();
        assertThrows(IllegalArgumentException.class, () -> service.removeApiKeys("", apiKeys));
    }

    @Test
    @DisplayName("removeApiKeys(): throws IllegalArgumentException when a apiKeys is null")
    void failureRemoveApiKeysNullApiKeys() {
        var serviceName = generateString();
        assertThrows(IllegalArgumentException.class, () -> service.removeApiKeys(serviceName, null));
    }

    @Test
    @DisplayName("removeApiKeys(): throws NotFoundException when a microservice not found")
    void failureRemoveApiKeysNotFoundMicroservice() {
        var serviceName = generateString();
        var apiKeys = Set.<UUID>of();
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.empty());
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> service.removeApiKeys(serviceName, apiKeys));
            verify(apiKeyAccessServiceMock, only()).find(anyString());
            verify(apiKeyAccessServiceMock, never()).deleteApiKeys(anyString(), anySet());
        });
    }

    @Test
    @DisplayName("removeApiKeys(): throws NotFoundException when api keys not found")
    void failureRemoveApiKeysNotFoundApiKeys() {
        var serviceName = generateString();
        var apiKeys = Set.<UUID>of();
        var microservice = microserviceDto(serviceName);
        when(apiKeyAccessServiceMock.find(anyString())).thenReturn(Optional.of(microservice));
        assertAll(() -> {
            assertThrows(NotFoundException.class, () -> service.removeApiKeys(serviceName, apiKeys));
            verify(apiKeyAccessServiceMock, only()).find(anyString());
            verify(apiKeyAccessServiceMock, never()).deleteApiKeys(anyString(), anySet());
        });
    }
}