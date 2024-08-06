
DROP TABLE IF EXISTS tipo_cambio;

CREATE TABLE tipo_cambio (
    id IDENTITY PRIMARY KEY,
    from_currency VARCHAR(255) NOT NULL,
    to_currency VARCHAR(255) NOT NULL,
    rate DOUBLE NOT NULL
);

DROP TABLE IF EXISTS audit;

CREATE TABLE audit (
    id IDENTITY PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    details VARCHAR(255)
);


INSERT INTO tipo_cambio (from_currency, to_currency, rate) VALUES ('USD', 'PEN', 3.5);
INSERT INTO tipo_cambio (from_currency, to_currency, rate) VALUES ('PEN', 'USD', 0.28);

