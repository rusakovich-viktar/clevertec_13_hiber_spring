create table if not exists house_history
(
    date      timestamp(6) not null,
    house_id  bigint       not null,
    id        bigserial
        primary key,
    person_id bigint       not null,
    type      varchar(255) not null
        constraint house_history_type_check
            check ((type)::text = ANY ((ARRAY ['OWNER'::character varying, 'TENANT'::character varying])::text[]))
);

alter table house_history
    owner to postgres;
