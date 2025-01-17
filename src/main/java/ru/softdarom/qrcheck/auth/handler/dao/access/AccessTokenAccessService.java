package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.dto.internal.AccessTokenDto;

import java.util.Collection;
import java.util.Optional;

public interface AccessTokenAccessService {

    Optional<AccessTokenDto> findByToken(String accessToken);

    Optional<AccessTokenDto> findById(Long id);

    AccessTokenDto save(AccessTokenDto dto);

    void delete(Long id);

    void deleteAll(Collection<AccessTokenDto> ids);

}