INSERT INTO users (id_number, email, pass, first_name, last_name, date_of_birth, department, title)
VALUES('SBT000001A', 'admin@sebit.com.tr', '$2a$10$raSBcKIe6rl8jIn9kLHW2uoI95fwjacQqOILXFkqVK6ovED6rcY/W' ,'gorkem', 'revi', '2001-02-06', 'admin', 'admin'),
      ('SBT000002U', 'user@sebit.com.tr', '$2a$10$LqVAlAyqj7EQQK9SUg0CXuj3JCeviyuvGdM1hVeL3tc/a6P.3VYua', 'gorkem', 'revi','1992-05-11', 'user', 'user');

INSERT INTO roles (name) 
VALUES ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO user_roles (user_id, role_id)
SELECT 'SBT000001A', id FROM roles WHERE name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT 'SBT000002U', id FROM roles WHERE name = 'ROLE_USER';