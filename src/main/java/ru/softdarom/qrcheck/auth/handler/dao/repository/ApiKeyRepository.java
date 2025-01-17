package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.ApiKeyEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

    @Query("select a from ApiKeyEntity a where a.microservice.name = :name and a.type = :type")
    Set<ApiKeyEntity> findAllByMicroserviceAndType(@Param("name") String name, @Param("type") ApiKeyType type);

    @Query("select a from ApiKeyEntity a where a.microservice.name = :name and a.key in :keys")
    Set<ApiKeyEntity> findAllByMicroserviceAndKeys(@Param("name") String name, @Param("keys") Collection<UUID> keys);


}