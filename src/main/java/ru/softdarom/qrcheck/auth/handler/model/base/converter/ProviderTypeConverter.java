package ru.softdarom.qrcheck.auth.handler.model.base.converter;

import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProviderTypeConverter implements AttributeConverter<ProviderType, String> {

    @Override
    public String convertToDatabaseColumn(ProviderType type) {
        return type.getType();
    }

    @Override
    public ProviderType convertToEntityAttribute(String type) {
        return ProviderType.typeOf(type);
    }
}