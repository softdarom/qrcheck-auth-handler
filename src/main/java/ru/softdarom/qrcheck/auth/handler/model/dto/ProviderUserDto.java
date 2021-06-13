package ru.softdarom.qrcheck.auth.handler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.BaseResponse;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

@Generated
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProviderUserDto extends BaseResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("secondName")
    private String secondName;

    @JsonProperty("email")
    private String email;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}