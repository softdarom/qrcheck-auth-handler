package ru.softdarom.qrcheck.auth.handler.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    private String message;

}