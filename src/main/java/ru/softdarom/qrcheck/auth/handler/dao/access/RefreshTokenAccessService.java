package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;

import java.util.Collection;
import java.util.Set;

public interface RefreshTokenAccessService {

    Set<RefreshTokenDto> find(Long userId, ProviderType provider);

    RefreshTokenDto save(RefreshTokenDto dto);

    Set<RefreshTokenDto> save(Collection<RefreshTokenDto> dtos);

    void deleteAll(Collection<RefreshTokenDto> dtos);
}