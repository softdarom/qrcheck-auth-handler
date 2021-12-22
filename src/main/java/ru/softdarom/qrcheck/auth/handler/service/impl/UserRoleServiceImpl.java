package ru.softdarom.qrcheck.auth.handler.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RoleDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.UserRoleResponse;
import ru.softdarom.qrcheck.auth.handler.service.UserRoleService;

import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SERVICE")
public class UserRoleServiceImpl implements UserRoleService {

    private final RoleAccessService roleAccessService;
    private final UserAccessService userAccessService;

    @Autowired
    UserRoleServiceImpl(RoleAccessService roleAccessService, UserAccessService userAccessService) {
        this.roleAccessService = roleAccessService;
        this.userAccessService = userAccessService;
    }

    @Override
    public UserRoleResponse getRoles(Long externalUserId) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not be null!");
        var roles =
                roleAccessService.findByExternalUserId(externalUserId).stream()
                        .map(RoleDto::getName)
                        .collect(Collectors.toSet());
        return new UserRoleResponse(externalUserId, roles);
    }

    @Override
    public UserRoleResponse changeRole(Long externalUserId, RoleType role) {
        Assert.notNull(externalUserId, "The 'externalUserId' must not be null!");
        Assert.notNull(role, "The 'role' must not be null!");
        var user = userAccessService.changeRole(externalUserId, role);
        return new UserRoleResponse(externalUserId, user.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet()));
    }
}