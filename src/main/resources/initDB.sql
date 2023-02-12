DROP TABLE IF EXISTS geodata;

CREATE TABLE IF NOT EXISTS geodata
(
    id        BIGSERIAL PRIMARY KEY,
    mac       VARCHAR(64)      NOT NULL,
    date_time TIMESTAMP        NOT NULL,
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);


