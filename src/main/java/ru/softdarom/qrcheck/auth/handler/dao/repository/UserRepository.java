package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByExternalUserId(Long externalUserId);

}