package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;

import java.util.Collection;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Set<RoleEntity> findAllByNameIn(Collection<String> names);

    @Query(
            value = "select r.id, r.name from roles r " +
                    "inner join users_roles ur on r.id = ur.role_id " +
                    "where ur.user_id = :userId",
            nativeQuery = true
    )
    Set<RoleEntity> findAllByUserId(@Param("userId") Long userId);

}