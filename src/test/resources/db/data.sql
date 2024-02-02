-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
truncate table house_history cascade;

truncate table house_owner cascade;

truncate table persons cascade;

truncate table houses cascade;

INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (200.0, 'Moscow', 'Russia', '2024-01-30T12:22:22', '1', 'Red Square',
        '550e8400-e29b-41d4-a716-446655440001'::uuid);

WITH inserted_house AS (
    INSERT INTO houses (area, city, country, create_date, number, street, uuid)
        VALUES (120.5, 'Minsk', 'Belarus', '2024-01-30T10:22:22', '123', 'Main Street',
                '550e8400-e29b-41d4-a716-446655440000'::uuid)
        RETURNING id),
     inserted_person AS (
         INSERT INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid,
                              house_id)
             SELECT '2024-02-01 10:10:10',                  -- create_date
                    'Иван',                                 -- name
                    '1234567',                              -- passport_number
                    'MP',                                   -- passport_series
                    'MALE',                                 -- sex
                    'Иванов',                               -- surname
                    '2024-02-01 10:10:10',                  -- update_date
                    '550e8400-e29b-41d4-a716-446655440010', -- uuid
                    id                                      -- house_id
             FROM inserted_house
             RETURNING id)
INSERT
INTO house_owner (house_id, owner_id)
SELECT inserted_house.id, inserted_person.id
FROM inserted_house,
     inserted_person;
