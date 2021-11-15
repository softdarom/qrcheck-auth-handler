package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleType roleType);

    @Query(
            value = "select r.id, r.name from roles r " +
                    "inner join users_roles ur on r.id = ur.role_id " +
                    "where ur.user_id = :userId",
            nativeQuery = true
    )
    Set<RoleEntity> findAllByUserId(@Param("userId") Long userId);

    @Query(
            value = "select r.id, r.name " +
                    "from roles r " +
                    "         inner join users_roles ur on r.id = ur.role_id " +
                    "         inner join users u on ur.user_id = u.id " +
                    "where u.external_user_id = :externalUserId",
            nativeQuery = true
    )
    Set<RoleEntity> findAllByExternalUserId(@Param("externalUserId") Long externalUserId);

}