package by.clevertec.house.cache.impl;

import by.clevertec.house.cache.Cache;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Реализация LRU (Least Recently Used) кэша.
 * Этот класс представляет собой кэш с ограниченной емкостью, который использует алгоритм LRU для удаления элементов при переполнении.
 * В кэше хранятся пары ключ-значение, и каждый ключ отслеживает время его последнего использования.
 * Когда кэш достигает своей максимальной емкости и нужно добавить новый элемент, удаляется элемент, который не использовался дольше всего.
 *
 * @param <K> Тип ключей, хранящихся в кэше.
 * @param <V> Тип значений, хранящихся в кэше.
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    private final int capacity;

    /**
     * Конструктор класса LRUCache.
     *
     * @param capacity Максимальный размер кэша.
     */
    public LruCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Определяет, должен ли быть удален старейший элемент. В этом случае элемент считается "старым", если размер кэша превышает его емкость.
     *
     * @param eldest Старейший элемент.
     * @return true, если размер кэша больше его емкости, иначе false.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    /**
     * Добавляет пару ключ-значение в кэш. Если ключ уже существует, обновляет его значение.
     *
     * @param key   Ключ элемента.
     * @param value Значение элемента.
     * @return Предыдущее значение, связанное с ключом, или null, если такого не было.
     */
    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    /**
     * Возвращает значение для данного ключа из кэша.
     *
     * @param key Ключ элемента.
     * @return Значение элемента или null, если ключ не найден.
     */
    @Override
    public V get(Object key) {
        return super.get(key);
    }

    /**
     * Удаляет элемент с указанным ключом из кэша.
     *
     * @param key Ключ элемента.
     * @return Значение удаленного элемента или null, если ключ не найден.
     */
    @Override
    public V remove(Object key) {
        return super.remove(key);
    }
}
