--liquibase formatted sql

--changeset eekovtun:1.0.0/grants/auth context:!local
--rollback revoke all on schema auth from auth_handler;
grant connect on database qrcheck to auth_handler;
grant usage on schema auth to auth_handler;
grant select, insert, update, delete on all tables in schema auth to auth_handler;
grant usage, select on all sequences in schema auth to auth_handler;