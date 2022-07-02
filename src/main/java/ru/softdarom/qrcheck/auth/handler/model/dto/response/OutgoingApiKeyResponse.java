package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.springframework.validation.annotation.Validated;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.ApiKeyDto;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.UUID;

@Validated
@Generated
@Data
@AllArgsConstructor
public class OutgoingApiKeyResponse {

    @NotEmpty
    @JsonProperty("serviceName")
    private final String serviceName;

    @NotEmpty
    @JsonProperty("outgoing")
    private final UUID outgoing;

    public OutgoingApiKeyResponse(MicroserviceDto dto) {
        this.serviceName = dto.getName();
        this.outgoing = dto.getApiKeys()
                .stream()
                .filter(it -> Objects.equals(ApiKeyType.OUTGOING, it.getType()))
                .map(ApiKeyDto::getKey)
                .findAny()
                .orElse(null);
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}
