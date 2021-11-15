package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Getter
@Setter
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractOAuth2AccessTokenResponse {

    private String token;

    protected AbstractOAuth2AccessTokenResponse(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}