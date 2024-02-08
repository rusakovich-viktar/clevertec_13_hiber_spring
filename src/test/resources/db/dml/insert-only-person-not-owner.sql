-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

truncate table house_owner cascade;

truncate table persons cascade;
-- select setval('persons_id_seq', 1, false);

truncate table houses cascade;
-- select setval('houses_id_seq', 1, false);

WITH inserted_house AS (
    INSERT INTO houses (area, city, country, create_date, number, street, uuid)
        VALUES (120.5, 'Minsk', 'Belarus', '2024-01-30T10:00:00', '123', 'Main Street',
                '550e8400-e29b-41d4-a716-446655440000'::uuid)
        RETURNING id)

INSERT
INTO persons (create_date, name, passport_number, passport_series, sex, surname, update_date, uuid, house_id)
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



