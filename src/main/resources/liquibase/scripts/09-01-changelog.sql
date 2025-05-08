-- liquibase formatted sql

-- changeset Amba404:1746723377588-1
CREATE TABLE rule_set
(
    id         UUID DEFAULT gen_random_uuid(),
    product_id UUID,
    rule       JSONB,
    CONSTRAINT pk_rule_set PRIMARY KEY (id)
);

-- changeset Amba404:1746723377588-2
ALTER TABLE rule_set
    ADD CONSTRAINT FK_RULE_SET_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES recommended_products (id) ON DELETE CASCADE;

