-- start Commenting moderator access

insert into SEC_ROLE
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, NAME, LOC_NAME, DESCRIPTION, ROLE_TYPE, IS_DEFAULT_ROLE, SYS_TENANT_ID, SECURITY_SCOPE)
values ('5f4f206f-9805-2b7f-1043-0580b0d348d5', 6, '2021-03-15 10:30:03', 'admin', '2021-03-16 12:29:54', 'admin', null, null, 'commenting-moderator', 'Commenting moderator access', 'Пользователи могут модерировать комментарии и просматривать экран комментариев', 0, null, null, 'GENERIC_UI');

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('b3585cb1-1b41-c0c2-071a-b5977c18ecce', 1, '2021-03-16 12:16:25', 'admin', '2021-03-16 12:16:25', null, null, null, 10, 'commentingdata_Comment.browse', 1, '5f4f206f-9805-2b7f-1043-0580b0d348d5')
,('d942e8ad-bc86-cc31-2157-1944cb2bc241', 1, '2021-03-16 12:16:25', 'admin', '2021-03-16 12:16:25', null, null, null, 10, 'commentingdata_Comment.edit', 1, '5f4f206f-9805-2b7f-1043-0580b0d348d5')
,('8942556f-1ada-bdaf-c918-207472b5f812', 1, '2021-03-16 12:16:25', 'admin', '2021-03-16 12:16:25', null, null, null, 10, 'commentingdata_Commenting', 1, '5f4f206f-9805-2b7f-1043-0580b0d348d5')
;

-- end Commenting moderator access

-- start Commenting allow-create access

insert into SEC_ROLE
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, NAME, LOC_NAME, DESCRIPTION, ROLE_TYPE, IS_DEFAULT_ROLE, SYS_TENANT_ID, SECURITY_SCOPE)
values ('376145fb-01fd-a1df-4d99-e0d58030c038', 5, '2021-03-16 12:15:46', 'admin', '2021-03-16 12:30:20', 'admin', null, null, 'commenting-allow-create', 'Commenting allow-create access', 'Пользователи могут создавать комментарии', 0, null, null, 'GENERIC_UI');

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('7d9e40c0-034d-17e3-56e4-41d3bfdb20c1', 1, '2021-03-16 12:15:46', 'admin', '2021-03-16 12:15:46', null, null, null, 10, 'commentingdata_Commenting', 1, '376145fb-01fd-a1df-4d99-e0d58030c038')
,('dbb3f0d1-a704-c4f8-9f3a-f68955f0f1c3', 1, '2021-03-16 12:15:46', 'admin', '2021-03-16 12:15:46', null, null, null, 10, 'commentingdata_Comment.edit', 1, '376145fb-01fd-a1df-4d99-e0d58030c038')
;

-- end Commenting allow-create access

-- start Commenting read-only access

insert into SEC_ROLE
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, NAME, LOC_NAME, DESCRIPTION, ROLE_TYPE, IS_DEFAULT_ROLE, SYS_TENANT_ID, SECURITY_SCOPE)
values ('45d4fa39-f542-4d5d-f4b9-482182d4d648', 7, '2021-03-16 12:18:58', 'admin', '2021-03-16 12:30:36', 'admin', null, null, 'commenting-read-only', 'Commenting read-only access', 'Пользователи могут только просматривать комментарии', 0, true, null, 'GENERIC_UI');

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('e374c4bf-4c4d-dcf1-c304-abe1b41f0e59', 1, '2021-03-16 12:18:58', 'admin', '2021-03-16 12:18:58', null, null, null, 10, 'commentingdata_Commenting', 1, '45d4fa39-f542-4d5d-f4b9-482182d4d648');

-- end Commenting read-only access
