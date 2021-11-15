package ru.softdarom.qrcheck.auth.handler.model.base.converter;

import org.springframework.core.convert.converter.Converter;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;

public class SpringRoleTypeConverter implements Converter<String, RoleType> {

    @Override
    public RoleType convert(String role) {
        return RoleType.apiRoleOf(role);
    }
}