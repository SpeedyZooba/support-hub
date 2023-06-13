CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE stat AS ENUM('PENDING', 'ANSWERED');

CREATE TABLE IF NOT EXISTS users (
    id_number       VARCHAR(10) PRIMARY KEY,
    email           TEXT,
    pass_hash       TEXT,
    first_name      TEXT,
    last_name       TEXT,
    date_of_birth   DATE,
    department      TEXT,
    title           TEXT
);

CREATE TABLE IF NOT EXISTS tickets (
    ticket_num      uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    category        TEXT,
    issue           TEXT,
    creator_id      TEXT,
    creation_date   DATE,
    ticket_status   stat,
    response_date   DATE
);
CREATE INDEX ON tickets(creator_id);

CREATE TABLE IF NOT EXISTS notices (
    notice_id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    notice_date     DATE,
    announcement    TEXT
);