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

insert into auth.user_token_info (user_id, provider, sub)
values (1, 'google', 'google_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (1, 'qrcheck', 'qrcheck_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (1, 'facebook', 'facebook_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (1, 'vkontakte', 'vkontakte_sub');

insert into auth.user_token_info (user_id, provider, sub)
values (2, 'google', 'google_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (2, 'qrcheck', 'qrcheck_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (2, 'facebook', 'facebook_sub');
insert into auth.user_token_info (user_id, provider, sub)
values (2, 'vkontakte', 'vkontakte_sub');

insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_google_1', 'google', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_qrcheck_1', 'qrcheck', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_facebook_1', 'facebook', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_vkontakte_1', 'vkontakte', '2021-01-01 00:00:00', true);

insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (2, 'refresh_token_google_2', 'google', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (2, 'refresh_token_qrcheck_2', 'qrcheck', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (2, 'refresh_token_facebook_2', 'facebook', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (2, 'refresh_token_vkontakte_2', 'vkontakte', '2021-01-01 00:00:00', true);

insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (1, 'access_token_google_1', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'google', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (2, 'access_token_qrcheck_2', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'qrcheck', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (3, 'access_token_facebook_3', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'facebook', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (4, 'access_token_vkontakte_4', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'vkontakte', true);

insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (5, 'access_token_google_5', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'google', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (6, 'access_token_qrcheck_6', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'qrcheck', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (7, 'access_token_facebook_7', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'facebook', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (8, 'access_token_vkontakte_8', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'vkontakte', true);