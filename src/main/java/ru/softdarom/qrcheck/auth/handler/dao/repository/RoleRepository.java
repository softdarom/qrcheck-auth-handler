package ru.softdarom.qrcheck.auth.handler.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softdarom.qrcheck.auth.handler.dao.entity.RoleEntity;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("select r from RoleEntity r where r.name = :name")
    Optional<RoleEntity> findByName(@Param("name") RoleType name);

    @Query("select r from RoleEntity r inner join r.users users where users.id = :id")
    Set<RoleEntity> findAllByUserId(@Param("id") Long id);

    @Query("select r from RoleEntity r inner join r.users users where users.externalUserId = :externalUserId")
    Set<RoleEntity> findAllByExternalUserId(@Param("externalUserId") Long externalUserId);

}