package ru.softdarom.qrcheck.auth.handler.model.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private RoleType name;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}