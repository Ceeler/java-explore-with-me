DROP SCHEMA IF EXISTS ewm CASCADE;
CREATE SCHEMA IF NOT EXISTS ewm;

CREATE type ewm.state AS ENUM ('PENDING', 'PUBLISHED', 'CANCELED');

CREATE type ewm.status AS ENUM ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS ewm.users
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR NOT NULL,
    email VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS ewm.categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS ewm.compilations
(
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ewm.events
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(120) NOT NULL,
    annotation   VARCHAR      NOT NULL,
    description  VARCHAR      NOT NULL,
    date         TIMESTAMP    NOT NULL,
    paid         BOOLEAN      NOT NULL,
    user_limit   INTEGER      NOT NULL DEFAULT 0,
    moderated    BOOLEAN      NOT NULL DEFAULT FALSE,
    state        ewm.state    NOT NULL DEFAULT 'PENDING',
    category_id  INTEGER      NOT NULL,
    creator_id   INTEGER      NOT NULL,
    event_lat    FLOAT8       NOT NULL,
    event_lon    FLOAT8       NOT NULL,
    created_on   TIMESTAMP    NOT NULL DEFAULT NOW(),
    published_on TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES ewm.categories (id),
    FOREIGN KEY (creator_id) REFERENCES ewm.users (id)
);

CREATE TABLE IF NOT EXISTS ewm.compilations_events
(
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS ewm.requests
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    event_id   BIGINT    NOT NULL,
    status     ewm.status         DEFAULT 'PENDING',
    created_on TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ewm.subscriptions
(
    user_id       BIGINT NOT NULL,
    subscriber_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, subscriber_id),
    FOREIGN KEY (user_id) REFERENCES ewm.users (id),
    FOREIGN KEY (subscriber_id) REFERENCES ewm.users (id)
)
