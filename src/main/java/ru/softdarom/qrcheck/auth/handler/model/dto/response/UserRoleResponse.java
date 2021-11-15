package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;

import java.util.Set;

@Data
@Generated
@EqualsAndHashCode(of = {"externalUserId", "roles"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleResponse {

    @JsonProperty("id")
    private Long externalUserId;

    @JsonProperty("roles")
    private Set<RoleType> roles;

    public UserRoleResponse(Long externalUserId, Set<RoleType> roles) {
        this.externalUserId = externalUserId;
        this.roles = roles;
    }
}