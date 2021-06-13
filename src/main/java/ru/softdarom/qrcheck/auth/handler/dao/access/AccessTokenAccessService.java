package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.dto.inner.AccessTokenDto;

import java.util.Optional;

public interface AccessTokenAccessService {

    Optional<AccessTokenDto> findByToken(String accessToken);

    Optional<AccessTokenDto> findById(Long id);

    AccessTokenDto save(AccessTokenDto dto);

}