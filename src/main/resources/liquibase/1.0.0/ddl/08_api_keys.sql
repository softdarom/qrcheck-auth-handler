--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/api_key_seq
--rollback drop sequence auth.api_key_seq;
create sequence auth.api_key_seq;

--changeset eekovtun:1.0.0/ddl/api_keys
--rollback drop table auth.api_keys;
create table auth.api_keys
(
    id              bigint  default nextval('auth.api_key_seq') not null
        constraint api_key_pk primary key,
    microservice_id bigint
        constraint api_key_microservice_id_fk
            references auth.microservices (id)                  not null,
    key             uuid                                        not null,
    type            varchar(255)                                not null,
    active          boolean default true
);

comment on table auth.api_keys is 'A table stores api keys for microservices';
comment on column auth.api_keys.id is 'A primary key of the table';
comment on column auth.api_keys.key is 'Api Key';
comment on column auth.api_keys.type is 'Type key, can be outgoing and incoming';
comment on column auth.users.active is 'A soft deleted flag: true - active, false - deleted';

--changeset eekovtun:1.0.0/ddl/api_keys_audit context:!local
--rollback drop trigger api_keys_audit on auth.api_keys;
create trigger api_keys_audit
    after insert or update or delete
    on auth.api_keys
    for each row
execute procedure audit.audit_func();