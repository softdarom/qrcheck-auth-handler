--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/user_seq
--rollback drop sequence auth.user_seq;
create sequence auth.user_seq;

--changeset eekovtun:1.0.0/ddl/users
--rollback drop table auth.users;
create table auth.users
(
    id               bigint       default nextval('auth.user_seq') not null
        constraint auth_pk primary key,
    external_user_id bigint                                        not null,
    created          timestamp(0) default now()                    not null,
    updated          timestamp(0) default now()                    not null,
    active           boolean      default true
);

create unique index users_external_user_id_uindex
    on auth.users (external_user_id);

comment on table auth.users is 'A table stores auth of auth';
comment on column auth.users.id is 'A primary key of the table';
comment on column auth.users.external_user_id is 'A user primary key of the user-handler service';
comment on column auth.users.created is 'Time of created';
comment on column auth.users.updated is 'Time of the last updated';
comment on column auth.users.active is 'A soft deleted flag: true - active, false - deleted';

--changeset eekovtun:1.0.0/ddl/users_audit context:!local
--rollback drop trigger users_audit on auth.users;
create trigger users_audit
    after insert or update or delete
    on auth.users
    for each row
execute procedure audit.audit_func();