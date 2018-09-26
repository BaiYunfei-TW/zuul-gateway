INSERT INTO users(id, username, password, enable) VALUES
('yunfei-id', 'yunfei', '123456', 1),
('admin-id', 'admin', '123456', 1);

INSERT INTO authorities(userId, authority) VALUES
('yunfei-id', 'USER'),
('admin-id', 'ADMIN');