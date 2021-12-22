package ru.softdarom.qrcheck.auth.handler.model.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "token", "provider", "active"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshTokenDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("token")
    private String token;

    @JsonProperty("issued")
    private LocalDateTime issued;

    @JsonProperty("provider")
    private ProviderType provider;

    @Builder.Default
    @JsonProperty("active")
    private ActiveType active = ActiveType.ENABLED;

    @JsonProperty("user")
    private UserDto user;

    @Builder.Default
    @JsonProperty("accessTokens")
    private Set<AccessTokenDto> accessTokens = new HashSet<>();

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}