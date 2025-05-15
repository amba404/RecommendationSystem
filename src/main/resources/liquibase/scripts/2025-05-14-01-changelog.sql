-- liquibase formatted sql

-- changeset Amba404:1747232526305-1
CREATE TABLE rule_stat
(
    rule_id    UUID NOT NULL,
    rule_count INTEGER,
    CONSTRAINT pk_rule_stat PRIMARY KEY (rule_id)
);

-- changeset Amba404:1747232526305-2
ALTER TABLE rule_stat
    ADD CONSTRAINT FK_RULE_STAT_ON_RULE FOREIGN KEY (rule_id) REFERENCES rule_set (id) ON DELETE CASCADE;

