-- Создание функции, которая будет вызываться триггером
CREATE OR REPLACE FUNCTION update_house_history() RETURNS TRIGGER AS $$
BEGIN
    -- Проверка на то, что произошло добавление или удаление записи
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO house_history (house_id, person_id, date, type)
        VALUES (NEW.house_id, NEW.owner_id, NOW(), 'OWNER');
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO house_history (house_id, person_id, date, type)
        VALUES (OLD.house_id, OLD.owner_id, NOW(), 'OWNER');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создание триггера, который вызывает функцию при добавлении или удалении записи в таблице house_owner
CREATE TRIGGER house_history_trigger
    AFTER INSERT OR DELETE ON house_owner
    FOR EACH ROW
EXECUTE FUNCTION update_house_history();
