package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Generated
@EqualsAndHashCode(callSuper = false)
public class GoogleTokenInfoResponse extends AbstractOAuth2TokenInfoResponse {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GoogleTokenInfoResponse(@JsonProperty("azp") String azp,
                                   @JsonProperty("aud") String aud,
                                   @JsonProperty("sub") String sub,
                                   @JsonProperty("scope") String scope,
                                   @JsonProperty("email") String email) {
        super(azp, aud, sub, scope, email);
    }
}