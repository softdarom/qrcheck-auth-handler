delete
from auth.access_tokens;
delete
from auth.refresh_tokens;
delete
from auth.users_roles;
delete
from auth.user_token_info;
delete
from auth.users;

alter sequence auth.access_token_seq restart with 1;
alter sequence auth.refresh_token_seq restart with 1;
alter sequence auth.user_role_seq restart with 1;
alter sequence auth.user_token_info_seq restart with 1;
alter sequence auth.user_seq restart with 1;