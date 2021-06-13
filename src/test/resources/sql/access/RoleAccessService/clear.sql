delete
from auth.users_roles;
delete
from auth.users;

alter sequence auth.user_role_seq restart with 1;
alter sequence auth.user_seq restart with 1;
