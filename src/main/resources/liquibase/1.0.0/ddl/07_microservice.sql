--liquibase formatted sql

--changeset eekovtun:1.0.0/ddl/microservice_seq
--rollback drop sequence auth.microservice_seq;
create sequence auth.microservice_seq;

--changeset eekovtun:1.0.0/ddl/microservices
--rollback drop table auth.microservices;
create table auth.microservices
(
    id     bigint  default nextval('auth.microservice_seq') not null
        constraint microservices_pk primary key,
    name   varchar(255)                                  not null,
    active boolean default true
);

create unique index microservice_name_uniq
    on auth.microservices (name);

comment on table auth.microservices is 'A table stores all microservices';
comment on column auth.microservices.id is 'A primary key of the table';
comment on column auth.microservices.name is 'Microservice name';
comment on column auth.microservices.active is 'A soft deleted flag: true - active, false - deleted';

--changeset eekovtun:1.0.0/ddl/service_name_audit context:!local
--rollback drop trigger microservices_audit on auth.microservices;
create trigger microservices_audit
    after insert or update or delete
    on auth.microservices
    for each row
execute procedure audit.audit_func();