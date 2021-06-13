package ru.softdarom.qrcheck.auth.handler.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.providerTokenDto;

@UnitTest
@DisplayName("UserTokenInfoDtoBuilder Unit Test")
class UserTokenInfoDtoBuilderTest {

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("build(): returns correct dto'")
    void successfulBuild(ProviderType provider) {
        var actual = new UserTokenInfoDtoBuilder(providerTokenDto(provider)).build();
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getSub());
            assertFalse(actual.getSub().isEmpty());
            assertEquals(provider, actual.getProvider());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("build(): throws IllegalArgumentException providerToken is null")
    void failureBuildNullProviderToken() {
        assertThrows(IllegalArgumentException.class, () -> new UserTokenInfoDtoBuilder(null).build());
    }

}