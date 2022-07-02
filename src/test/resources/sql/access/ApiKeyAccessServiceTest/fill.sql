insert into auth.microservices (name) values ('test-service');

insert into auth.api_keys (microservice_id, key, type)
select m.id, '22bc357f-677a-4fa4-a28c-ae74fa870da3', 'incoming'
from auth.microservices m
where m.name = 'test-service';

insert into auth.api_keys (microservice_id, key, type)
select m.id, 'd29124a8-c8ea-49ee-bf08-2eea8ad74b30', 'incoming'
from auth.microservices m
where m.name = 'test-service';