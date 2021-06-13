insert into auth.users (external_user_id, active)
values (1, true);
insert into auth.users (external_user_id, active)
values (2, true);
insert into auth.users (external_user_id, active)
values (3, true);

insert into auth.users_roles (user_id, role_id)
values (1, 1);
insert into auth.users_roles (user_id, role_id)
values (2, 1);
insert into auth.users_roles (user_id, role_id)
values (3, 1);