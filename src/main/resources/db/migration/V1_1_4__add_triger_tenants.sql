-- Создание функции, которая будет вызываться триггером
CREATE OR REPLACE FUNCTION update_tenant_history() RETURNS TRIGGER AS
$$
BEGIN
    -- Проверка на то, что произошло обновление поля house_id
    INSERT INTO house_history (house_id, person_id, date, type)
    VALUES (NEW.house_id, NEW.id, NOW(), 'TENANT');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создание триггера, который вызывает функцию при обновлении записи в таблице persons
CREATE TRIGGER tenant_history_trigger
    AFTER UPDATE OF house_id
    ON persons
    FOR EACH ROW
EXECUTE FUNCTION update_tenant_history();
