package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Generated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class TokenUserInfoResponse {

    @JsonProperty("user")
    private final ProviderUserDto user;

    @JsonProperty("token")
    private final ProviderTokenDto token;

    public TokenUserInfoResponse(ProviderUserDto user, ProviderTokenDto token) {
        this.user = user;
        this.token = token;
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}