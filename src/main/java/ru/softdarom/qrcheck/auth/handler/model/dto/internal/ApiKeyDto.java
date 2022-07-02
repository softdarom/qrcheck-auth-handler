package ru.softdarom.qrcheck.auth.handler.model.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.UUID;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "key", "type", "active"})
public class ApiKeyDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("key")
    private UUID key;

    @JsonProperty("type")
    private ApiKeyType type;

    @Builder.Default
    private ActiveType active = ActiveType.ENABLED;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

    public static ApiKeyDto empty(ApiKeyType type) {
        return ApiKeyDto.builder()
                .id(0L)
                .type(type)
                .active(ActiveType.DISABLED)
                .build();
    }
}
