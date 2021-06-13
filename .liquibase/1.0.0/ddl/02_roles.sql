--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/role_seq
--rollback drop sequence auth.role_seq;
create sequence auth.role_seq;

--changeset eekovtun:1.0.0/ddl/roles
--rollback drop table auth.roles;
create table auth.roles
(
    id   bigint default nextval('auth.role_seq') not null
        constraint roles_pk primary key,
    name varchar(100)                            not null
);

create unique index roles_name_uindex
    on auth.roles (name);

comment on table auth.roles is 'A table stores inner roles the service';
comment on column auth.roles.id is 'A primary key of the table';
comment on column auth.roles.name is 'A role name';