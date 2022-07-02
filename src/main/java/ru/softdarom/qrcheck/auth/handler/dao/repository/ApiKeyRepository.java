package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.ApiKeyEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;

import java.util.Set;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long>, JpaSpecificationExecutor<ApiKeyEntity> {

    @Query(
            """
            select a from ApiKeyEntity a
            where a.microservice.name = :name
              and a.type = :type
              and a.active = ru.softdarom.qrcheck.auth.handler.model.base.ActiveType.ENABLED
            """)
    Set<ApiKeyEntity> findAllByMicroserviceAndType(@Param("name") String name, @Param("type") ApiKeyType type);

}