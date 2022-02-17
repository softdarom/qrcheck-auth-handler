--liquibase formatted sql

--changeset eekovtun:1.0.0/grants/viewer context:!local
--rollback revoke all on schema auth from viewer;
grant usage on schema auth to viewer;
grant select, insert, update on all tables in schema auth to viewer;
grant usage, select on all sequences in schema auth to viewer;