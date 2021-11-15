package ru.softdarom.qrcheck.auth.handler.model.base.converter;

import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType role) {
        return role.getRole();
    }

    @Override
    public RoleType convertToEntityAttribute(String type) {
        return RoleType.roleOf(type);
    }
}