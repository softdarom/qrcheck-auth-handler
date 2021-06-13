package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;

import java.util.Optional;

public interface UserHandlerService {

    Optional<Long> saveUser(ProviderUserDto request);

}