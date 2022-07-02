package ru.softdarom.qrcheck.auth.handler.model.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.HashSet;
import java.util.Set;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "active"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MicroserviceDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Builder.Default
    @JsonProperty("active")
    private ActiveType active = ActiveType.ENABLED;

    @Builder.Default
    @JsonProperty("apiKeys")
    private Set<ApiKeyDto> apiKeys = new HashSet<>();

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}
