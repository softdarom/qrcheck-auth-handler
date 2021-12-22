--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/user_token_info_seq
--rollback drop sequence auth.user_token_info_seq;
create sequence auth.user_token_info_seq;

--changeset eekovtun:1.0.0/ddl/user_token_info
--rollback drop table auth.user_token_info;
create table auth.user_token_info
(
    id       bigint default nextval('auth.user_token_info_seq') not null
        constraint user_token_info_pk primary key,
    user_id  bigint
        constraint access_token_user_id_fk
            references auth.users (id)                          not null,
    provider varchar(100)                                       not null,
    sub      varchar(255)                                       not null
);

create unique index user_token_info_user_id_provider_uniq
    on auth.user_token_info (user_id, provider);

create trigger user_token_info_audit
    after insert or update or delete
    on auth.user_token_info
    for each row
execute procedure audit.audit_func();

comment on table auth.user_token_info is 'A table stores additional information of user';
comment on column auth.user_token_info.id is 'A primary key of the table';
comment on column auth.user_token_info.user_id is 'Reference on linked a user';
comment on column auth.user_token_info.provider is 'An external oAuth service name';
comment on column auth.user_token_info.sub is 'The sub. See https://openid.net/specs/openid-connect-core-1_0.html#IDToken';