package ru.softdarom.qrcheck.auth.handler.service;

import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.UserRoleResponse;

public interface UserRoleService {

    UserRoleResponse getRoles(Long userId);

    UserRoleResponse changeRole(Long userId, RoleType role);

}