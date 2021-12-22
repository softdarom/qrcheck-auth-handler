--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/user_role_seq
--rollback drop sequence auth.user_role_seq;
create sequence auth.user_role_seq;

--changeset eekovtun:1.0.0/ddl/users_roles
--rollback drop table auth.users_roles;
create table auth.users_roles
(
    id      bigint default nextval('auth.user_role_seq') not null
        constraint users_roles_pk primary key,
    user_id bigint                                       not null
        constraint users_roles_user_id_fk
            references auth.users,
    role_id bigint                                       not null
        constraint users_roles_role_id_fk
            references auth.roles
);

create unique index auth_roles_ids_fk_uniq
    on auth.users_roles (user_id, role_id);

create trigger users_roles_audit
    after insert or update or delete
    on auth.users_roles
    for each row
execute procedure audit.audit_func();

comment on table auth.users_roles is 'A table stores links auth and roles';
comment on column auth.users_roles.id is 'A primary key of the table';
comment on column auth.users_roles.user_id is 'Reference on a user id';
comment on column auth.users_roles.role_id is 'Reference on a role id';