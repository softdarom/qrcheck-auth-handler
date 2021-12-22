package ru.softdarom.qrcheck.auth.handler.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Slf4j(topic = "BUILDER")
public final class RefreshTokenDtoBuilder {

    private final ProviderTokenDto.RefreshToken refreshToken;
    private final ProviderType provider;

    public RefreshTokenDtoBuilder(ProviderTokenDto.RefreshToken refreshToken, ProviderType provider) {
        Assert.notNull(refreshToken, "The 'refreshToken' must not be null!");
        Assert.notNull(provider, "The 'provider' must not be null!");
        this.refreshToken = refreshToken;
        this.provider = provider;
    }

    public RefreshTokenDto build() {
        LOGGER.debug("Building a RefreshTokenDto by {} for provider {}", JsonHelper.asJson(refreshToken), provider);
        return RefreshTokenDto.builder()
                .token(refreshToken.getToken())
                .issued(refreshToken.getIssuedAt())
                .provider(provider)
                .build();
    }
}