package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.UserEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.externalUserId = :externalUserId")
    Optional<UserEntity> findByExternalUserId(@Param("externalUserId") Long externalUserId);

    @Query("select u from UserEntity u where u.externalUserId in :externalUserIds")
    Set<UserEntity> findByExternalUserIds(@Param("externalUserIds") Collection<Long> externalUserIds);



}