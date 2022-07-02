package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ApiKeyAccessService {

    Set<UUID> find(String serviceName, ApiKeyType apiKeyType);

    Optional<MicroserviceDto> find(String serviceName);

    MicroserviceDto save(MicroserviceDto dto);

    void deleteApiKeys(String serviceName, Set<UUID> apiKeys);

}
