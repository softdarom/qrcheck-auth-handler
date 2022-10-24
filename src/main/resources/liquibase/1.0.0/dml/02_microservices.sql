--liquibase formatted sql

--changeset eekovtun:1.0.0/dml/microservices context:local
--rollback delete from auth.microservices where id != null;
insert into auth.microservices (name) values ('auth-handler');