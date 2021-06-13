package ru.softdarom.qrcheck.auth.handler.dao.access;

import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RoleDto;

import java.util.Set;

public interface RoleAccessService {

    Set<RoleDto> defaultRoles();

    Set<RoleDto> findByUserId(Long userId);

}