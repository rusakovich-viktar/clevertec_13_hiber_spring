CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--1 значение
-- Вставляем данные в таблицу houses
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (120.5, 'Minsk', 'Belarus', CURRENT_TIMESTAMP, '123', 'Main Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'John', '123456', 'AB', 'MALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

--2 значение
-- Вставляем данные в таблицу houses
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (85.7, 'Grodno', 'Belarus', CURRENT_TIMESTAMP, '456', 'Second Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Jane', '789012', 'CD', 'FEMALE', 'Smith', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

--вставляем дом без жильцов
-- Вставляем данные в таблицу houses
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (100.0, 'Brest', 'Belarus', CURRENT_TIMESTAMP, '789', 'Third Street', uuid_generate_v4());

--дом и два владельца

-- Вставляем данные в таблицу houses
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (150.0, 'Vitebsk', 'Belarus', CURRENT_TIMESTAMP, '321', 'Fourth Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons для первого владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Alice Johnson', '345678', 'EF', 'FEMALE', 'Johnson', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для первого владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для второго владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Bob Johnson', '901234', 'GH', 'MALE', 'Johnson', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для второго владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));