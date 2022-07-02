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

--changeset eekovtun:1.0.0/dml/api_keys context:test
--rollback delete from auth.api_keys where id != null;
insert into auth.api_keys (microservice_id, key, type)
select m.id, '771fa126-6bd8-464e-86ed-783695721bba', 'incoming'
from auth.microservices m
where m.name = 'auth-handler';

insert into auth.api_keys (microservice_id, key, type)
select m.id, 'd29124a8-c8ea-49ee-bf08-2eea8ad74b30', 'outgoing'
from auth.microservices m
where m.name = 'auth-handler';

--changeset eekovtun:1.0.0/dml/api_keys context:prod
--rollback delete from auth.api_keys where id != null;
insert into auth.api_keys (microservice_id, key, type)
select m.id, '2c9afcea-67d9-4f2e-8d8f-05d42dc3225f', 'incoming'
from auth.microservices m
where m.name = 'auth-handler';

insert into auth.api_keys (microservice_id, key, type)
select m.id, '6db49698-b5df-4a10-8403-d91da8d00fa1', 'outgoing'
from auth.microservices m
where m.name = 'auth-handler';