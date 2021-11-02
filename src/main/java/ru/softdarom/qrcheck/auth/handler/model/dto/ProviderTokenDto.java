package ru.softdarom.qrcheck.auth.handler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Generated
@Data
public class ProviderTokenDto {

    @NotEmpty
    @JsonProperty("sub")
    private String sub;

    @NotNull
    @JsonProperty("provider")
    private ProviderType provider;

    @NotNull
    @JsonProperty("accessToken")
    private AccessToken accessToken;

    @JsonProperty("refreshToken")
    private RefreshToken refreshToken;

    @Data
    public static class AccessToken {

        @NotEmpty
        @JsonProperty("token")
        private String token;

        @NotNull
        @JsonProperty("issuedAt")
        private LocalDateTime issuedAt;

        @NotNull
        @JsonProperty("expiresAt")
        private LocalDateTime expiresAt;

    }

    @Data
    public static class RefreshToken {

        @NotEmpty
        @JsonProperty("token")
        private String token;

        @NotNull
        @JsonProperty("issuedAt")
        private LocalDateTime issuedAt;

    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}