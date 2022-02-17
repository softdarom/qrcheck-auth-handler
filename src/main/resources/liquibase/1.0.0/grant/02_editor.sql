--liquibase formatted sql

--changeset eekovtun:1.0.0/grants/editor context:!local
--rollback revoke all on schema auth from editor;
grant usage on schema auth to editor;
grant select, insert, update, delete on all tables in schema auth to editor;
grant usage, select on all sequences in schema auth to editor;