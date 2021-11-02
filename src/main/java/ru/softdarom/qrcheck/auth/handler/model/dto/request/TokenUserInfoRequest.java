package ru.softdarom.qrcheck.auth.handler.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.validation.constraints.NotNull;

@Generated
@Data
public class TokenUserInfoRequest {

    @NotNull
    @JsonProperty("user")
    private final ProviderUserDto user;

    @NotNull
    @JsonProperty("token")
    private final ProviderTokenDto token;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}