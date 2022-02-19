CREATE OR REPLACE VIEW developer_pagination AS
SELECT
D.id AS id,
D.id AS developer_id,
U.username AS user_username,
D.email AS developer_email,
IF((SELECT UR.roles FROM user_roles UR WHERE UR.user_id = U.id AND UR.roles = 'ROLE_ADMIN') IS NOT NULL, 'ROLE_ADMIN', IF((SELECT UR.roles FROM user_roles UR WHERE UR.user_id = U.id AND UR.roles = 'ROLE_DEVELOPER') IS NOT NULL, 'ROLE_DEVELOPER', NULL)) AS user_role
FROM developer D
JOIN user U ON D.user_id = U.id;