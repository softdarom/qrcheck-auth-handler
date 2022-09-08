package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.UserRoleResponse;

import java.util.Collection;
import java.util.Set;

public interface UserRoleService {

    UserRoleResponse getRoles(Long userId);

    Set<UserRoleResponse> getRoles(Collection<Long> externalUserIds);

    UserRoleResponse changeRole(Long userId, RoleType role);

}