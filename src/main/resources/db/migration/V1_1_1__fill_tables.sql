CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Дом 1 с двумя владельцами (персон 1 и 2)
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (120.5, 'Minsk', 'Belarus', CURRENT_TIMESTAMP, '123', 'Main Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons для первого владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'John', '123456', 'AB', 'MALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для первого владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для второго владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Jane', '654321', 'CD', 'FEMALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для второго владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Дом 2 с одним владельцем (персон 3) и одним жильцом (персон 4)
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (150.0, 'Gomel', 'Belarus', CURRENT_TIMESTAMP, '456', 'Second Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons для владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Bob', '456789', 'IJ', 'MALE', 'Smith', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для жильца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Alice Johnson', '345678', 'EF', 'FEMALE', 'Johnson', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Дом 3 без жильцов
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (100.0, 'Brest', 'Belarus', CURRENT_TIMESTAMP, '789', 'Third Street', uuid_generate_v4());

-- Дом 4 с одним владельцем (персон 5) и одним жильцом (персон 6)
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (85.7, 'Grodno', 'Belarus', CURRENT_TIMESTAMP, '456', 'Second Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons для владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Bob Johnson', '901234', 'GH', 'MALE', 'Johnson', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для жильца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Anna', '223456', 'BB', 'FEMALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Дом 5 с двумя владельцами (персон 7 и 8) и двумя жильцами (персон 9 и 10)
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (200.0, 'Minsk', 'Belarus', CURRENT_TIMESTAMP, '123', 'Fifth Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons для первого владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Lera', '154321', 'SS', 'FEMALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для первого владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для второго владельца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Vasya', '456784', 'IW', 'FEMALE', 'Mask', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner для второго владельца
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

-- Вставляем данные в таблицу persons для первого жильца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Ivan', '789012', 'KL', 'MALE', 'Ivanov', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу persons для второго жильца
INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'Petr', '901234', 'LM', 'MALE', 'Petrov', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));
