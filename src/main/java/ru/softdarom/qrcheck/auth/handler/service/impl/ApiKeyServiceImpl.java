package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.ApiKeyAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.IncomingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.OutgoingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.IncomingApiKeyResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.OutgoingApiKeyResponse;
import ru.softdarom.qrcheck.auth.handler.service.ApiKeyService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class ApiKeyServiceImpl implements ApiKeyService {

    private static final String NOT_NULL_REQUEST_MESSAGE = "The 'request' must not be null!";
    private static final String NOT_EMPTY_SERVICE_NAME_MESSAGE = "The 'serviceName' must not be null or empty!";
    private final ApiKeyAccessService apiKeyAccessService;

    @Autowired
    ApiKeyServiceImpl(ApiKeyAccessService apiKeyAccessService) {
        this.apiKeyAccessService = apiKeyAccessService;
    }

    @Override
    public Set<UUID> getIncoming(String serviceName) {
        Assert.hasText(serviceName, NOT_EMPTY_SERVICE_NAME_MESSAGE);
        LOGGER.debug("Getting incoming api key for service [{}]", serviceName);
        var incoming = apiKeyAccessService.find(serviceName, ApiKeyType.INCOMING);
        if (incoming.isEmpty()) {
            throw new NotFoundException("Not found api keys for a service: " + serviceName);
        }
        return incoming;
    }

    @Override
    public UUID getOutgoing(String serviceName) {
        Assert.hasText(serviceName, NOT_EMPTY_SERVICE_NAME_MESSAGE);
        LOGGER.debug("Getting outgoing api key for service [{}]", serviceName);
        var outgoing = apiKeyAccessService.find(serviceName, ApiKeyType.OUTGOING);
        return outgoing.stream().findAny().orElseThrow(() -> new NotFoundException("Not found api key for a service: " + serviceName));
    }

    @Override
    public IncomingApiKeyResponse saveIncoming(IncomingApiKeyRequest request) {
        Assert.notNull(request, NOT_NULL_REQUEST_MESSAGE);
        var microserviceOptional = apiKeyAccessService.find(request.getServiceName());
        if (microserviceOptional.isEmpty()) {
            LOGGER.warn("Microservice [{}] not found. The one will be created.", request.getServiceName());
            var microservice =
                    MicroserviceDto.builder()
                            .apiKeys(buildApiKeys(request.getIncoming(), ApiKeyType.INCOMING))
                            .name(request.getServiceName())
                            .build();
            var savedMicroservice = apiKeyAccessService.save(microservice);
            return new IncomingApiKeyResponse(savedMicroservice);
        } else {
            var microservice = microserviceOptional.get();
            microservice.getApiKeys().addAll(buildApiKeys(request.getIncoming(), ApiKeyType.INCOMING));
            var savedMicroservice = apiKeyAccessService.save(microservice);
            return new IncomingApiKeyResponse(savedMicroservice);
        }
    }

    @Override
    public OutgoingApiKeyResponse saveOutgoing(OutgoingApiKeyRequest request) {
        Assert.notNull(request, NOT_NULL_REQUEST_MESSAGE);
        var microserviceOptional = apiKeyAccessService.find(request.getServiceName());
        if (microserviceOptional.isEmpty()) {
            LOGGER.warn("Microservice [{}] not found. The one will be created.", request.getServiceName());
            var microservice =
                    MicroserviceDto.builder()
                            .apiKeys(buildApiKeys(Set.of(request.getOutgoing()), ApiKeyType.OUTGOING))
                            .name(request.getServiceName())
                            .build();
            var savedMicroservice = apiKeyAccessService.save(microservice);
            return new OutgoingApiKeyResponse(savedMicroservice);
        } else {
            var microservice = microserviceOptional.get();
            microservice.getApiKeys().addAll(buildApiKeys(Set.of(request.getOutgoing()), ApiKeyType.OUTGOING));
            var savedMicroservice = apiKeyAccessService.save(microservice);
            return new OutgoingApiKeyResponse(savedMicroservice);
        }
    }

    @Override
    public void removeApiKeys(String serviceName, Set<UUID> apiKeys) {
        Assert.hasText(serviceName, NOT_EMPTY_SERVICE_NAME_MESSAGE);
        Assert.notNull(apiKeys, "The 'apiKeys' must not be null!");
        var microservice =
                apiKeyAccessService.find(serviceName).orElseThrow(() -> new NotFoundException("Microservice not found!"));
        var anyApiKeyExisted = microservice.getApiKeys().stream()
                .map(ApiKeyDto::getKey)
                .anyMatch(apiKeys::contains);
        if (!anyApiKeyExisted) {
            throw new NotFoundException("Api keys not found into existed api key for a microservice!");
        }
        apiKeyAccessService.deleteApiKeys(serviceName, apiKeys);
    }

    private Set<ApiKeyDto> buildApiKeys(Set<UUID> apiKeys, ApiKeyType type) {
        return apiKeys
                .stream()
                .map(it ->
                        ApiKeyDto.builder()
                                .key(it)
                                .type(type)
                                .build()
                )
                .collect(Collectors.toSet());
    }
}
