insert into auth.users (external_user_id, active)
values (1, true);

insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_google_1', 'google', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_qrcheck_1', 'qrcheck', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_facebook_1', 'facebook', '2021-01-01 00:00:00', true);
insert into auth.refresh_tokens (user_id, token, provider, issued, active)
values (1, 'refresh_token_vkontakte_1', 'vkontakte', '2021-01-01 00:00:00', true);

insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (1, 'access_token_google_1', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'google', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (2, 'access_token_qrcheck_2', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'qrcheck', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (3, 'access_token_facebook_3', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'facebook', true);
insert into auth.access_tokens (refresh_token_id, token, issued, expires, provider, active)
values (4, 'access_token_vkontakte_4', '2021-01-01 00:00:00', '2021-01-01 01:00:00', 'vkontakte', true);