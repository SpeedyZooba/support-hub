CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE stat AS ENUM('PENDING', 'ANSWERED');

CREATE TABLE IF NOT EXISTS users (
    id_number       VARCHAR(10) PRIMARY KEY,
    email           TEXT,
    pass            VARCHAR(255),
    first_name      TEXT,
    last_name       TEXT,
    date_of_birth   DATE,
    department      VARCHAR(63),
    title           VARCHAR(63)
);
CREATE INDEX ON users(email);

CREATE TABLE IF NOT EXISTS roles (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id         VARCHAR(10) REFERENCES users(id_number),
    role_id         INT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)  
);

CREATE TABLE IF NOT EXISTS tickets (
    ticket_num      uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    category        VARCHAR(63),
    issue           TEXT,
    creator_id      VARCHAR(10),
    creation_date   DATE,
    ticket_status   stat,
    response_date   DATE
);
CREATE INDEX ON tickets(creator_id);

CREATE TABLE IF NOT EXISTS tokens (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owned_by        VARCHAR(10),
    token_string    VARCHAR(64),
    expiry_date     TIMESTAMP(6),
    is_used         BOOLEAN
);
CREATE INDEX ON tokens(owned_by);

CREATE TABLE IF NOT EXISTS notices (
    notice_id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    notice_date     DATE,
    announcement    TEXT
);