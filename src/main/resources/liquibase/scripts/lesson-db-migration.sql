-- liquibase formatted sql

-- changeset Amba404:1
CREATE TABLE IF NOT EXISTS recommended_products
(
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL unique,
    text TEXT
);

