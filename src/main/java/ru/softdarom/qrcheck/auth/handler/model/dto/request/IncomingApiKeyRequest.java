package ru.softdarom.qrcheck.auth.handler.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;
import org.springframework.validation.annotation.Validated;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Validated
@Generated
@Data
public class IncomingApiKeyRequest {

    @NotEmpty
    @JsonProperty("serviceName")
    private final String serviceName;

    @NotNull
    @JsonProperty("incoming")
    private final Set<UUID> incoming;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}
