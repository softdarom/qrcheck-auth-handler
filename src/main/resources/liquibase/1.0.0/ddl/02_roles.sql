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

create unique index roles_name_uniq
    on auth.roles (name);

comment on table auth.roles is 'A table stores inner roles the service';
comment on column auth.roles.id is 'A primary key of the table';
comment on column auth.roles.name is 'A role name';

--changeset eekovtun:1.0.0/ddl/roles_audit context:!local
--rollback drop trigger roles_audit on auth.roles;
create trigger roles_audit
    after insert or update or delete
    on auth.roles
    for each row
execute procedure audit.audit_func();