package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.request.IncomingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.OutgoingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.IncomingApiKeyResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.OutgoingApiKeyResponse;

import java.util.Set;
import java.util.UUID;

public interface ApiKeyService {

    Set<UUID> getIncoming(String serviceName);

    UUID getOutgoing(String serviceName);

    IncomingApiKeyResponse saveIncoming(IncomingApiKeyRequest request);

    OutgoingApiKeyResponse saveOutgoing(OutgoingApiKeyRequest request);

    void removeApiKeys(String serviceName, Set<UUID> apiKeys);

}
