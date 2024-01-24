create table if not exists houses
(
    id          bigserial
        primary key,
    area        double precision not null,
    city        varchar(255)     not null,
    country     varchar(255)     not null,
    create_date timestamp(6)     not null,
    number      varchar(255)     not null,
    street      varchar(255)     not null,
    uuid        uuid             not null
        constraint uk_96kgokym5invmnlxei1tdxs2w
            unique
);

alter table houses
    owner to postgres;

create table if not exists persons
(
    id              bigserial
        primary key,
    create_date     timestamp(6) not null,
    name            varchar(25) not null,
    passport_number varchar(7) not null,
    passport_series varchar(2) not null,
    sex             varchar(6) not null,
    surname         varchar(255) not null,
    update_date     timestamp(6) not null,
    uuid            uuid         not null,
    house_id        bigint       not null
        constraint fkjpvj6kr0i5qlh395t0680d6dg
            references houses,
    constraint uk5ke6ttyu61axxog5avjvnf2yo
        unique (passport_series, passport_number)
);

alter table persons
    owner to postgres;

create table if not exists house_owner
(
    house_id bigint not null
        constraint fkbywq04tar3xgvn8flaulfvyin
            references houses,
    owner_id bigint not null
        constraint fk4ghooy9o8vvcc9pw19110lb67
            references persons,
    primary key (house_id, owner_id)
);

alter table house_owner
    owner to postgres;

