CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Вставляем данные в таблицу houses
INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (120.5, 'Minsk', 'Belarus', CURRENT_TIMESTAMP, '123', 'Main Street', uuid_generate_v4());

-- Получаем ID только что вставленного дома
SELECT currval(pg_get_serial_sequence('houses', 'id'));

-- Вставляем данные в таблицу persons
INSERT INTO persons (create_date, name, passportnumber, passportseries, sex, surname, update_date, uuid, house_id)
VALUES (CURRENT_TIMESTAMP, 'John Doe', '123456', 'AB', 'MALE', 'Doe', CURRENT_TIMESTAMP, uuid_generate_v4(),
        currval(pg_get_serial_sequence('houses', 'id')));

-- Вставляем данные в таблицу house_owner
INSERT INTO house_owner (house_id, owner_id)
VALUES (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));
