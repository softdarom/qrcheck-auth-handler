package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserDto;

import java.util.Optional;

public interface UserAccessService {

    Optional<UserDto> findByExternalUserId(Long externalUserId);

    UserDto save(UserDto dto);

    UserDto changeRole(Long externalUserId, RoleType role);

}