package ru.softdarom.qrcheck.auth.handler.model.dto.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "provider", "sub"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTokenInfoDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("provider")
    private ProviderType provider;

    @JsonProperty("user")
    private UserDto user;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}