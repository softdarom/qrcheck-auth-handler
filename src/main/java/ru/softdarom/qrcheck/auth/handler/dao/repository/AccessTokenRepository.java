package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.AccessTokenEntity;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, Long> {

    @Query("select a from AccessTokenEntity a where a.token = :token")
    Optional<AccessTokenEntity> findByToken(@Param("token") String token);


}