package ru.softdarom.qrcheck.auth.handler.builder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.AccessTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2AccessTokenResponse;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j(topic = "AUTH-HANDLER-BUILDER")
public final class AccessTokenDtoBuilder {

    public static class ByProviderTokenDto {

        private final ProviderTokenDto.AccessToken accessToken;
        private final ProviderType provider;

        public ByProviderTokenDto(ProviderTokenDto.AccessToken accessToken, ProviderType provider) {
            Assert.notNull(accessToken, "The 'accessToken' must not be null!");
            Assert.notNull(provider, "The 'provider' must not be null!");
            this.accessToken = accessToken;
            this.provider = provider;
        }

        public AccessTokenDto build() {
            LOGGER.debug("Building an AccessTokenDto by {} for provider {}", JsonHelper.asJson(accessToken), provider);
            return AccessTokenDto.builder()
                    .token(accessToken.getToken())
                    .issued(accessToken.getIssuedAt())
                    .expires(accessToken.getExpiresAt())
                    .provider(provider)
                    .build();
        }
    }

    public static class ByOAuth2AccessToken {

        private final AbstractOAuth2AccessTokenResponse oAuth2AccessToken;
        private final RefreshTokenDto refreshToken;

        public ByOAuth2AccessToken(AbstractOAuth2AccessTokenResponse oAuth2AccessToken, RefreshTokenDto refreshToken) {
            Assert.notNull(oAuth2AccessToken, "The 'oAuth2AccessToken' must not be null!");
            Assert.notNull(refreshToken, "The 'refreshToken' must not be null!");
            this.oAuth2AccessToken = oAuth2AccessToken;
            this.refreshToken = refreshToken;
        }

        public AccessTokenDto build() {
            LOGGER.debug("Building an AccessTokenDto by {} for provider {}", JsonHelper.asJson(oAuth2AccessToken), refreshToken.getProvider());
            return AccessTokenDto.builder()
                    .token(oAuth2AccessToken.getToken())
                    .issued(LocalDateTime.now().minusMinutes(1L))
                    .expires(LocalDateTime.now().minusMinutes(1L).plusHours(1L))
                    .provider(refreshToken.getProvider())
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}