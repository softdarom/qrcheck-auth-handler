package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RefreshTokenEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;

import java.util.Objects;
import java.util.function.BiConsumer;

@Component
public class RefreshTokenDtoMapper extends AbstractDtoMapper<RefreshTokenEntity, RefreshTokenDto> {

    private final AccessTokenDtoMapper accessTokenMapper;

    @Autowired
    protected RefreshTokenDtoMapper(ModelMapper modelMapper, AccessTokenDtoMapper accessTokenMapper) {
        super(modelMapper);
        this.accessTokenMapper = accessTokenMapper;
    }

    @Override
    protected void setupMapper() {
        modelMapper
                .createTypeMap(destinationClass, sourceClass)
                .addMappings(mapping -> mapping.skip(RefreshTokenEntity::setAccessTokens))
                .setPostConverter(toSourceConverter(new SourceConverter()));
        modelMapper
                .createTypeMap(sourceClass, destinationClass)
                .addMappings(mapping -> mapping.skip(RefreshTokenDto::setAccessTokens))
                .setPostConverter(toDestinationConverter(new DestinationConverter()));
    }

    public class SourceConverter implements BiConsumer<RefreshTokenDto, RefreshTokenEntity> {

        @Override
        public void accept(RefreshTokenDto destination, RefreshTokenEntity source) {
            setAccessTokens(destination, source);
        }

        private void setAccessTokens(RefreshTokenDto destination, RefreshTokenEntity source) {
            if (Objects.nonNull(destination.getAccessTokens())) {
                var accessTokens = destination.getAccessTokens();
                accessTokens.forEach(it -> it.setRefreshToken(null));
                source.setAccessTokens(accessTokenMapper.convertToSources(accessTokens));
                accessTokens.forEach(it -> it.setRefreshToken(destination));
            }
        }
    }

    public class DestinationConverter implements BiConsumer<RefreshTokenEntity, RefreshTokenDto> {

        @Override
        public void accept(RefreshTokenEntity source, RefreshTokenDto destination) {
            whenNotNull(source.getAccessTokens(), it -> destination.setAccessTokens(accessTokenMapper.convertToDestinations(it)));
        }
    }
}