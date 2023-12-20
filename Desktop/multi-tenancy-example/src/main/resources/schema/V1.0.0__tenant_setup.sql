create table if not exists fruits
(
    id   bigserial primary key,
    name character varying(255)
);

insert into fruits(id, name)
VALUES (nextval('fruits_id_seq'), 'Apple');