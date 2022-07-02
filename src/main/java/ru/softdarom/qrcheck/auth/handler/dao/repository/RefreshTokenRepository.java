package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RefreshTokenEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;

import java.util.Set;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Query("select r from RefreshTokenEntity r where r.user.id = :userId and r.provider = :provider")
    Set<RefreshTokenEntity> findAllByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") ProviderType provider);

}