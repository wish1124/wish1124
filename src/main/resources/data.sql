INSERT INTO school (id, name, address) VALUES (1, 'Sample School', '123 Example St.');

INSERT INTO user (id, email, password, name, role, status, school_id)
VALUES
  (1, 'admin@example.com', 'password', 'Admin User', 'ADMIN', 'APPROVED', null),
  (2, 'manager@example.com', 'password', 'Manager User', 'MANAGER', 'APPROVED', 1),
  (3, 'user@example.com', 'password', 'General User', 'USER', 'PENDING', 1);
