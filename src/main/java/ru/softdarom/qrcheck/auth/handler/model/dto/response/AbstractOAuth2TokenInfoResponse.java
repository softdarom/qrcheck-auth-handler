package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import ru.softdarom.qrcheck.auth.handler.model.base.TokenValidType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractOAuth2TokenInfoResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("azp")
    private String azp;

    @JsonProperty("aud")
    private String aud;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email")
    private String email;

    @JsonProperty("scopes")
    private Set<String> scopes;

    @JsonProperty("valid")
    private TokenValidType valid;

    private AbstractOAuth2TokenInfoResponse() {
    }

    protected AbstractOAuth2TokenInfoResponse(String azp, String aud, String sub, String scopes, String email) {
        this.azp = azp;
        this.aud = aud;
        this.sub = sub;
        this.email = email;
        this.scopes = Optional.ofNullable(scopes).map(it -> Arrays.stream(it.split(" ")).collect(Collectors.toSet())).orElse(Set.of());
        setValid(TokenValidType.VALID);
    }

    public static AbstractOAuth2TokenInfoResponse expiredToken() {
        return new ExpiredTokenInfoResponse();
    }

    public static AbstractOAuth2TokenInfoResponse incorrectToken() {
        return new IncorrectTokenInfoResponse();
    }

    public void addScopes(Set<String> scopes) {
        this.scopes.addAll(scopes);
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

    public static class ExpiredTokenInfoResponse extends AbstractOAuth2TokenInfoResponse {

        public ExpiredTokenInfoResponse() {
            setValid(TokenValidType.EXPIRED);
        }
    }

    public static class IncorrectTokenInfoResponse extends AbstractOAuth2TokenInfoResponse {

        public IncorrectTokenInfoResponse() {
            setValid(TokenValidType.INCORRECT);
        }
    }
}