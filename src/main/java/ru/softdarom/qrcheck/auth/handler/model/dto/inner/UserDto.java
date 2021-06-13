package ru.softdarom.qrcheck.auth.handler.model.dto.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.HashSet;
import java.util.Set;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "externalUserId", "active"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("externalUserId")
    private Long externalUserId;

    @Builder.Default
    @JsonProperty("active")
    private ActiveType active = ActiveType.ENABLED;

    @Builder.Default
    @JsonProperty("refreshTokens")
    private Set<RefreshTokenDto> refreshTokens = new HashSet<>();

    @Builder.Default
    @JsonProperty("tokenInfo")
    private Set<UserTokenInfoDto> tokenInfo = new HashSet<>();

    @Builder.Default
    @JsonProperty("roles")
    private Set<RoleDto> roles = new HashSet<>();

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}