package ru.softdarom.qrcheck.auth.handler.model.dto.inner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import java.util.HashSet;
import java.util.Set;

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
    private String name;

    @Builder.Default
    @JsonProperty("users")
    private Set<UserDto> users = new HashSet<>();

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}