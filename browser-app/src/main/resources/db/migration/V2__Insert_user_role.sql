INSERT INTO t_role (id, name)
VALUES (1, 'USER_ROLE')
ON CONFLICT (id) DO NOTHING;