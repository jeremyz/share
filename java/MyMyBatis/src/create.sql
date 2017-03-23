CREATE TABLE models
(
    id      serial NOT NULL,
    name    text,
    CONSTRAINT pk_id PRIMARY KEY (id)
);

INSERT INTO models(name) VALUES ('asynk');
INSERT INTO models(name) VALUES ('fake');
SET AUTOCOMMIT FALSE;
