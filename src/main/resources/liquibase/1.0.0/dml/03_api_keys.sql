--liquibase formatted sql

--changeset eekovtun:1.0.0/dml/api_keys context:local
--rollback delete from auth.api_keys where id != null;
insert into auth.api_keys (microservice_id, key, type)
select m.id, '7c1ac776-0532-4be2-9f29-f637da759421', 'incoming'
from auth.microservices m
where m.name = 'auth-handler';

insert into auth.api_keys (microservice_id, key, type)
select m.id, 'd88d2dab-4002-4d7c-b259-d9682df4f22b', 'outgoing'
from auth.microservices m
where m.name = 'auth-handler';