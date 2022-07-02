package ru.softdarom.qrcheck.auth.handler.model.base.converter;

import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ApiKeyTypeConverter implements AttributeConverter<ApiKeyType, String> {

    @Override
    public String convertToDatabaseColumn(ApiKeyType type) {
        return type.getType();
    }

    @Override
    public ApiKeyType convertToEntityAttribute(String type) {
        return ApiKeyType.typeOf(type);
    }
}