package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RefreshTokenEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;

import java.util.Set;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Set<RefreshTokenEntity> findAllByUserIdAndProvider(Long userId, ProviderType provider);

}