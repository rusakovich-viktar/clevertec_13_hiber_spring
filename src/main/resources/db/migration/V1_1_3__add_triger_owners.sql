-- Создание функции, которая будет вызываться триггером
CREATE OR REPLACE FUNCTION update_house_history() RETURNS TRIGGER AS
$$
BEGIN
    -- Проверка на то, что произошло добавление записи
    INSERT INTO house_history (house_id, person_id, date, type)
    VALUES (NEW.house_id, NEW.owner_id, NOW(), 'OWNER');
    RETURN new;
END;
$$ LANGUAGE plpgsql;

-- Создание триггера, который вызывает функцию при добавлении или изменении записи в таблице house_owner
CREATE TRIGGER house_history_trigger
    AFTER INSERT OR UPDATE OF owner_id
    ON house_owner
    FOR EACH ROW
EXECUTE FUNCTION update_house_history();
