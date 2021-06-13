--liquibase formatted sql

--changeset eekovtun:1.0.0/dml/roles
--rollback delete from auth.roles where id != null;
insert into auth.roles (name) values ('ROLE_USER');