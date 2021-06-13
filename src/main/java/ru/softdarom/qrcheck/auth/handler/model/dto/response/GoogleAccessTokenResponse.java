package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Generated
@EqualsAndHashCode(callSuper = false)
public class GoogleAccessTokenResponse extends AbstractOAuth2AccessTokenResponse {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GoogleAccessTokenResponse(@JsonProperty("access_token") String token) {
        super(token);
    }
}