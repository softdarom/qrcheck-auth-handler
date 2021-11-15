package ru.softdarom.qrcheck.auth.handler.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.ApiGetRoles;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.ApiSwitchRole;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.UserRoleResponse;
import ru.softdarom.qrcheck.auth.handler.service.UserRoleService;

@Tag(name = "UserRoles", description = "Контроллер управления ролями пользователей")
@RestController
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Autowired
    UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @ApiGetRoles
    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<UserRoleResponse> getRoles(@RequestHeader("X-Application-Version") String version,
                                                     @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userRoleService.getRoles(userId));
    }

    @ApiSwitchRole
    @PutMapping("/users/{userId}/roles/{role}")
    public ResponseEntity<UserRoleResponse> changeRole(@RequestHeader("X-Application-Version") String version,
                                                       @PathVariable("userId") Long userId,
                                                       @PathVariable("role") RoleType role) {
        return ResponseEntity.ok(userRoleService.changeRole(userId, role));
    }
}