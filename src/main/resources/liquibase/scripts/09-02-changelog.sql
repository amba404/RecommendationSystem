-- liquibase formatted sql

-- changeset Amba404:1746772328332-1
ALTER TABLE rule_set
    ALTER COLUMN product_id SET NOT NULL;

-- changeset Amba404:1746772987989-2
ALTER TABLE rule_set
    ALTER COLUMN rule SET NOT NULL;
