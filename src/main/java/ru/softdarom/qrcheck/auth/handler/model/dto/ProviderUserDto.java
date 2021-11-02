package ru.softdarom.qrcheck.auth.handler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;
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

    @JsonProperty("picture")
    private String picture;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}