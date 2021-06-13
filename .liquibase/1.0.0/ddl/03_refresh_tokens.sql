--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/refresh_token_seq
--rollback drop sequence auth.refresh_token_seq;
create sequence auth.refresh_token_seq;

--changeset eekovtun:1.0.0/ddl/refresh_tokens
--rollback drop table auth.refresh_tokens;
create table auth.refresh_tokens
(
    id       bigint  default nextval('auth.refresh_token_seq') not null
        constraint refresh_tokens_pk primary key,
    user_id  bigint
        constraint access_token_user_id_fk
            references auth.users (id)                         not null,
    token    varchar(2000)                                     not null,
    provider varchar(100)                                      not null,
    issued   timestamp(0)                                      not null,
    active   boolean default true
);

create unique index refresh_tokens_token_provider_uniq
    on auth.refresh_tokens (token, provider);

comment on table auth.refresh_tokens is 'A table stores refresh auth from external oAuth services such as Google, Facebook etc';
comment on column auth.refresh_tokens.id is 'A primary key of the table';
comment on column auth.refresh_tokens.user_id is 'Reference on linked a user';
comment on column auth.refresh_tokens.token is 'A refresh token value';
comment on column auth.refresh_tokens.provider is 'An external oAuth service name';
comment on column auth.refresh_tokens.issued is 'Time of issuance a refresh token';
comment on column auth.refresh_tokens.active is 'A soft deleted flag: true - active, false - deleted';