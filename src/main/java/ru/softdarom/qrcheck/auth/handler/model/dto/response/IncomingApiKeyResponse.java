package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Generated
@Data
@AllArgsConstructor
public class IncomingApiKeyResponse {

    @NotEmpty
    @JsonProperty("serviceName")
    private final String serviceName;

    @NotEmpty
    @JsonProperty("incoming")
    private final Set<UUID> incoming;

    public IncomingApiKeyResponse(MicroserviceDto dto) {
        this.serviceName = dto.getName();
        this.incoming = dto.getApiKeys()
                .stream()
                .filter(it -> Objects.equals(ApiKeyType.INCOMING, it.getType()))
                .map(ApiKeyDto::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}
