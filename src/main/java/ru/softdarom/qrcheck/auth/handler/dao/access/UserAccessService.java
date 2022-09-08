package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.UserDto;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserAccessService {

    Optional<UserDto> findByExternalUserId(Long externalUserId);

    Set<UserDto> findByExternalUserIds(Collection<Long> externalUserIds);

    UserDto save(UserDto dto);

    UserDto changeRole(Long externalUserId, RoleType role);

    void delete(Long userId);

}