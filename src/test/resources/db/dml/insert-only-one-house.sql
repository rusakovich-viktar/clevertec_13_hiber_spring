-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

truncate table house_owner cascade;

truncate table persons cascade;
-- select setval('persons_id_seq', 1, false);

truncate table houses cascade;
-- select setval('houses_id_seq', 1, false);

INSERT INTO houses (area, city, country, create_date, number, street, uuid)
VALUES (120.5, 'Minsk', 'Belarus', '2024-01-30T10:00:00', '123', 'Main Street',
        '550e8400-e29b-41d4-a716-446655440000'::uuid)

