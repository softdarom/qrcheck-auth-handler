package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;

import java.util.Optional;

public interface UserHandlerService {

    boolean isExistedUser(Long externalUserId);

    Optional<Long> saveUser(ProviderUserDto request);

}