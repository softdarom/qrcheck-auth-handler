package ru.softdarom.qrcheck.auth.handler.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderTokenDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserTokenInfoDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Slf4j(topic = "BUILDER")
public final class UserTokenInfoDtoBuilder {

    private final ProviderTokenDto providerToken;

    public UserTokenInfoDtoBuilder(ProviderTokenDto providerToken) {
        Assert.notNull(providerToken, "The 'providerToken' must not be null!");
        this.providerToken = providerToken;
    }

    public UserTokenInfoDto build() {
        LOGGER.debug("Создание объекта UserTokenInfoDto из {}", JsonHelper.asJson(providerToken));
        return UserTokenInfoDto.builder()
                .sub(providerToken.getSub())
                .provider(providerToken.getProvider())
                .build();
    }
}