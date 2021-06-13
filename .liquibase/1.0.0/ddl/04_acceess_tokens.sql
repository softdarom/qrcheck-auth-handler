--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/access_token_seq
--rollback drop sequence auth.access_token_seq;
create sequence auth.access_token_seq;

--changeset eekovtun:1.0.0/ddl/access_tokens
--rollback drop table auth.access_tokens;
create table auth.access_tokens
(
    id               bigint  default nextval('auth.access_token_seq') not null
        constraint access_tokens_pk primary key,
    refresh_token_id bigint
        constraint access_token_refresh_token_id_fk
            references auth.refresh_tokens (id)                       not null,
    token            varchar(2000)                                    not null,
    issued           timestamp(0)                                     not null,
    expires          timestamp(0)                                     not null,
    provider         varchar(100)                                     not null,
    active           boolean default true
);

create index access_tokens_token_index
    on auth.access_tokens (token);
create unique index access_tokens_token_provider_uniq
    on auth.access_tokens (token, provider);

comment on table auth.access_tokens is 'A table stores access auth from external oAuth services such as Google, Facebook etc';
comment on column auth.access_tokens.id is 'A primary key of the table';
comment on column auth.access_tokens.refresh_token_id is 'Reference on linked a refresh token';
comment on column auth.access_tokens.token is 'An access token value';
comment on column auth.access_tokens.issued is 'Time of issuance an access token';
comment on column auth.access_tokens.expires is 'Time of the end an access token';
comment on column auth.access_tokens.provider is 'An external oAuth service name';
comment on column auth.access_tokens.active is 'A soft deleted flag: true - active, false - deleted';
