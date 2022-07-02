-- Delete all except of liquibase dml-scripts
delete from auth.api_keys a
where a.key not in ('7c1ac776-0532-4be2-9f29-f637da759421', 'd88d2dab-4002-4d7c-b259-d9682df4f22b');

delete from auth.microservices m
where m.name <> 'auth-handler';

-- Restart all except of liquibase dml-scripts
alter sequence auth.microservice_seq restart with 2;
alter sequence auth.api_key_seq restart with 3;