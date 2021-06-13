package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.UserDto;

import java.util.function.BiConsumer;

@Component
public class UserDtoMapper extends AbstractDtoMapper<UserEntity, UserDto> {

    private final RefreshTokenDtoMapper refreshTokenMapper;
    private final RoleDtoMapper roleMapper;
    private final UserTokenInfoDtoMapper userTokenInfoMapper;

    @Autowired
    protected UserDtoMapper(ModelMapper modelMapper,
                            RefreshTokenDtoMapper refreshTokenMapper, RoleDtoMapper roleMapper,
                            UserTokenInfoDtoMapper userTokenInfoMapper) {
        super(modelMapper);
        this.refreshTokenMapper = refreshTokenMapper;
        this.roleMapper = roleMapper;
        this.userTokenInfoMapper = userTokenInfoMapper;
    }

    @Override
    protected void setupMapper() {
        modelMapper
                .createTypeMap(destinationClass, sourceClass)
                .addMappings(mapping -> {
                    mapping.skip(UserEntity::setRefreshTokens);
                    mapping.skip(UserEntity::setRoles);
                    mapping.skip(UserEntity::setTokenInfo);
                })
                .setPostConverter(toSourceConverter(new SourceConverter()));
        modelMapper
                .createTypeMap(sourceClass, destinationClass)
                .addMappings(mapping -> {
                    mapping.skip(UserDto::setRefreshTokens);
                    mapping.skip(UserDto::setRoles);
                    mapping.skip(UserDto::setTokenInfo);
                })
                .setPostConverter(toDestinationConverter(new DestinationConverter()));
    }

    public class SourceConverter implements BiConsumer<UserDto, UserEntity> {

        @Override
        public void accept(UserDto destination, UserEntity source) {
            whenNotNull(destination.getRefreshTokens(), it -> source.setRefreshTokens(refreshTokenMapper.convertToSources(it)));
            whenNotNull(destination.getRoles(), it -> source.setRoles(roleMapper.convertToSources(it)));
            whenNotNull(destination.getTokenInfo(), it -> source.setTokenInfo(userTokenInfoMapper.convertToSources(it)));
        }
    }

    public class DestinationConverter implements BiConsumer<UserEntity, UserDto> {

        @Override
        public void accept(UserEntity source, UserDto destination) {
            whenNotNull(source.getRefreshTokens(), it -> destination.setRefreshTokens(refreshTokenMapper.convertToDestinations(it)));
            whenNotNull(source.getRoles(), it -> destination.setRoles(roleMapper.convertToDestinations(it)));
            whenNotNull(source.getTokenInfo(), it -> destination.setTokenInfo(userTokenInfoMapper.convertToDestinations(it)));
        }
    }
}