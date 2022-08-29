package ru.softdarom.qrcheck.auth.handler.dao.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateString;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.microserviceDto;

@SpringIntegrationTest
@Sql(
        scripts = "classpath:sql/access/ApiKeyAccessServiceTest/clear.sql",
        config = @SqlConfig(transactionMode = INFERRED), executionPhase = AFTER_TEST_METHOD
)
@DisplayName("ApiKeyAccessService Spring Integration Test")
class ApiKeyAccessServiceTest {

    private static final String MICROSERVICE_NAME_FOR_DELETING = "test-service";
    private static final Set<UUID> API_KEYS_FOR_DELETING =
            Set.of(UUID.fromString("22bc357f-677a-4fa4-a28c-ae74fa870da3"), UUID.fromString("d29124a8-c8ea-49ee-bf08-2eea8ad74b30"));

    private static final ApiKeyType DEFAULT_TYPE = ApiKeyType.INCOMING;

    @Value("${spring.application.name}")
    private String defaultServiceName;

    @Autowired
    private ApiKeyAccessService apiKeyAccessService;


    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ApiKeyType.class)
    @DisplayName("find(string, apikeytype): returns values of api key")
    void successfulFindStringApiKeyType(ApiKeyType type) {
        var actual = assertDoesNotThrow(() -> apiKeyAccessService.find(defaultServiceName, type));
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        });
    }

    @Test
    @DisplayName("find(string): returns a not empty value")
    void successfulFindStringNotEmpty() {
        var actual = assertDoesNotThrow(() -> apiKeyAccessService.find(defaultServiceName));
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertNotNull(actual.get().getId());
        });
    }

    @Test
    @DisplayName("find(string): returns an empty value")
    void successfulFindStringEmpty() {
        var notExisted = generateString();
        var actual = assertDoesNotThrow(() -> apiKeyAccessService.find(notExisted));
        assertAll(() -> {
            assertNotNull(actual);
            assertTrue(actual.isEmpty());
        });
    }

    @Test
    @DisplayName("save(microservicedto): returns a saved dto without apikeys")
    void successfulSaveMicroserviceDtoWithoutApiKeys() {
        var dto = microserviceDto();
        var actual = assertDoesNotThrow(() -> apiKeyAccessService.save(dto));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getId());
        });
    }

    @Test
    @DisplayName("save(microservicedto): returns a saved dto with apikeys")
    void successfulSaveMicroserviceDtoWithApiKeys() {
        var dto = microserviceDto(generateString());
        var actual = assertDoesNotThrow(() -> apiKeyAccessService.save(dto));
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getApiKeys());
            assertFalse(actual.getApiKeys().isEmpty());
        });
    }

    @Test
    @DisplayName("deleteApiKeys(string, set): deletes api keys")
    @Sql(
            scripts = "classpath:sql/access/ApiKeyAccessServiceTest/fill.sql",
            config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD
    )
    void successfulDeleteApiKeys() {
        assertDoesNotThrow(() -> apiKeyAccessService.deleteApiKeys(MICROSERVICE_NAME_FOR_DELETING, API_KEYS_FOR_DELETING));
        assertAll(() -> {
            var existed = apiKeyAccessService.find(MICROSERVICE_NAME_FOR_DELETING, DEFAULT_TYPE);
            assertTrue(existed.isEmpty());
        });
    }

    @Test
    @DisplayName("deleteApiKeys(string, set): does nothing when a api keys not found")
    @Sql(
            scripts = "classpath:sql/access/ApiKeyAccessServiceTest/fill.sql",
            config = @SqlConfig(transactionMode = INFERRED), executionPhase = BEFORE_TEST_METHOD
    )
    void successfulDeleteApiKeysNotFoundApiKeys() {
        var notExisted = Set.of(UUID.randomUUID(), UUID.randomUUID());
        assertDoesNotThrow(() -> apiKeyAccessService.deleteApiKeys(MICROSERVICE_NAME_FOR_DELETING, notExisted));
        assertAll(() -> {
            var existed = apiKeyAccessService.find(MICROSERVICE_NAME_FOR_DELETING, DEFAULT_TYPE);
            assertFalse(existed.isEmpty());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("find(string, apikeytype): throws IllegalArgumentException when a serviceName is null")
    void failureFindStringApiKeyTypeNullServiceName() {
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.find(null, ApiKeyType.INCOMING));
    }

    @Test
    @DisplayName("find(string, apikeytype): throws IllegalArgumentException when a serviceName is empty")
    void failureFindStringApiKeyTypeEmptyServiceName() {
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.find("", ApiKeyType.INCOMING));
    }

    @Test
    @DisplayName("find(string, apikeytype): throws IllegalArgumentException when an apiKeyType is null")
    void failureFindStringApiKeyTypeNullApiKeyType() {
        var serviceName = generateString();
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.find(serviceName, null));
    }

    @Test
    @DisplayName("find(string): throws IllegalArgumentException when a serviceName is null")
    void failureFindStringNullServiceName() {
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.find(null));
    }

    @Test
    @DisplayName("find(string): throws IllegalArgumentException when a serviceName is empty")
    void failureFindStringEmptyServiceName() {
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.find(""));
    }

    @Test
    @DisplayName("save(microservicedto): throws IllegalArgumentException when a dto is null")
    void failureSaveMicroserviceDtoNullDto() {
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.save(null));
    }

    @Test
    @DisplayName("deleteApiKeys(string, set): throws IllegalArgumentException when a serviceName is null")
    void failureDeleteApiKeysStringSetNullServiceName() {
        var set = Set.<UUID>of();
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.deleteApiKeys(null, set));
    }

    @Test
    @DisplayName("deleteApiKeys(string, set): throws IllegalArgumentException when a serviceName is empty")
    void failureDeleteApiKeysStringSetEmptyServiceName() {
        var set = Set.<UUID>of();
        assertThrows(IllegalArgumentException.class, () -> apiKeyAccessService.deleteApiKeys("", set));
    }
}