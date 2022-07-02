package ru.softdarom.qrcheck.auth.handler.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softdarom.qrcheck.auth.handler.dao.entity.MicroserviceEntity;
import ru.softdarom.qrcheck.auth.handler.mapper.AbstractDtoMapper;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.MicroserviceDto;

import java.util.function.BiConsumer;

@Component
public class MicroserviceDtoMapper extends AbstractDtoMapper<MicroserviceEntity, MicroserviceDto> {

    private final ApiKeyDtoMapper apiKeyMapper;

    @Autowired
    protected MicroserviceDtoMapper(ModelMapper modelMapper, ApiKeyDtoMapper apiKeyMapper) {
        super(modelMapper);
        this.apiKeyMapper = apiKeyMapper;
    }

    @Override
    protected void setupMapper() {
        modelMapper
                .createTypeMap(destinationClass, sourceClass)
                .addMappings(mapping -> mapping.skip(MicroserviceEntity::setApiKeys))
                .setPostConverter(toSourceConverter(new SourceConverter()));

        modelMapper
                .createTypeMap(sourceClass, destinationClass)
                .addMappings(mapping -> mapping.skip(MicroserviceDto::setApiKeys))
                .setPostConverter(toDestinationConverter(new DestinationConverter()));
    }

    public class SourceConverter implements BiConsumer<MicroserviceDto, MicroserviceEntity> {

        @Override
        public void accept(MicroserviceDto destination, MicroserviceEntity source) {
            whenNotNull(destination.getApiKeys(), it -> source.setApiKeys(apiKeyMapper.convertToSources(it)));
        }
    }

    public class DestinationConverter implements BiConsumer<MicroserviceEntity, MicroserviceDto> {

        @Override
        public void accept(MicroserviceEntity source, MicroserviceDto destination) {
            whenNotNull(source.getApiKeys(), it -> destination.setApiKeys(apiKeyMapper.convertToDestinations(it)));
        }
    }
}