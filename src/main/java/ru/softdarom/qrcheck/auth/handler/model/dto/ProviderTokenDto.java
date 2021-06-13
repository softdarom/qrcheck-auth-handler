package ru.softdarom.qrcheck.auth.handler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.time.LocalDateTime;

@Generated
@Data
public class ProviderTokenDto {

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("provider")
    private ProviderType provider;

    @JsonProperty("accessToken")
    private AccessToken accessToken;

    @JsonProperty("refreshToken")
    private RefreshToken refreshToken;

    @Data
    public static class AccessToken {

        @JsonProperty("token")
        private String token;

        @JsonProperty("issuedAt")
        private LocalDateTime issuedAt;

        @JsonProperty("expiresAt")
        private LocalDateTime expiresAt;

    }

    @Data
    public static class RefreshToken {

        @JsonProperty("token")
        private String token;

        @JsonProperty("issuedAt")
        private LocalDateTime issuedAt;

    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}