package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.MicroserviceEntity;

import java.util.Optional;

public interface MicroserviceRepository extends JpaRepository<MicroserviceEntity, Long> {

    @Query("select m from MicroserviceEntity m where m.name = :name")
    Optional<MicroserviceEntity> findByName(@Param("name") String name);

}