-- Food Waste Redistribution database schema
-- Safe to re-run: uses CREATE TABLE IF NOT EXISTS

CREATE TABLE IF NOT EXISTS accounts (
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    password_hash VARCHAR(64)  NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    status        VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS listings (
    id             SERIAL PRIMARY KEY,
    provider_id    INTEGER      NOT NULL REFERENCES accounts(id),
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    quantity       INTEGER      NOT NULL,
    pickup_window  VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
    id           SERIAL PRIMARY KEY,
    listing_id   INTEGER     NOT NULL REFERENCES listings(id),
    customer_id  INTEGER     NOT NULL REFERENCES accounts(id),
    status       VARCHAR(20) NOT NULL
);
