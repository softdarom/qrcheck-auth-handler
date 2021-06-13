package ru.softdarom.qrcheck.auth.handler.model.dto.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.time.LocalDateTime;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "token", "provider", "active"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessTokenDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("token")
    private String token;

    @JsonProperty("issued")
    private LocalDateTime issued;

    @JsonProperty("expires")
    private LocalDateTime expires;

    @JsonProperty("provider")
    private ProviderType provider;

    @Builder.Default
    @JsonProperty("active")
    private ActiveType active = ActiveType.ENABLED;

    @JsonProperty("refreshToken")
    private RefreshTokenDto refreshToken;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}