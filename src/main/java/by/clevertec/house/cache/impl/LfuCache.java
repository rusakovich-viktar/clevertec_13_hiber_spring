package by.clevertec.house.cache.impl;

import by.clevertec.house.cache.Cache;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Реализация LFU (Least Frequently Used) кэша.
 * Этот класс представляет собой кэш с ограниченной емкостью, который использует алгоритм LFU для удаления элементов при переполнении.
 * В кэше хранятся пары ключ-значение, и каждому ключу сопоставляется счетчик, который отслеживает, как часто этот ключ используется.
 * Когда кэш достигает своей максимальной емкости и нужно добавить новый элемент, удаляется элемент с наименьшим счетчиком использования.
 *
 * @param <K> Тип ключей, хранящихся в кэше.
 * @param <V> Тип значений, хранящихся в кэше.
 */
public class LfuCache<K, V> implements Cache<K, V> {

    private final Map<K, V> vals;
    private final Map<K, Integer> counts;
    private final Map<Integer, LinkedHashSet<K>> lists;
    private int min = -1;
    private final int capacity;

    /**
     * Конструктор класса LFUCache.
     *
     * @param capacity Максимальный размер кэша.
     */
    public LfuCache(int capacity) {
        this.capacity = capacity;
        vals = new HashMap<>();
        counts = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1, new LinkedHashSet<>());
    }

    /**
     * Добавляет пару ключ-значение в кэш. Если ключ уже существует, обновляет его значение и увеличивает счетчик использования.
     * Если кэш полон, удаляет наименее часто используемый элемент перед добавлением нового.
     *
     * @param key   Ключ элемента.
     * @param value Значение элемента.
     * @return Значение элемента.
     */
    @Override
    public V put(K key, V value) {
        if (capacity <= 0) {
            return null;
        }
        if (vals.containsKey(key)) {
            vals.put(key, value);
            get(key);
            return value;
        }
        if (vals.size() >= capacity) {
            K evict = lists.get(min).iterator().next();
            lists.get(min).remove(evict);
            vals.remove(evict);
        }
        vals.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
        return value;
    }

    /**
     * Возвращает значение для данного ключа из кэша и увеличивает счетчик использования ключа.
     *
     * @param key Ключ элемента.
     * @return Значение элемента или null, если ключ не найден.
     */
    @Override
    public V get(Object key) {
        if (!vals.containsKey(key)) {
            return null;
        }
        int count = counts.get(key);
        counts.put((K) key, count + 1);
        lists.get(count).remove(key);
        if (count == min && lists.get(count).size() == 0) {
            min++;
        }
        if (!lists.containsKey(count + 1)) {
            lists.put(count + 1, new LinkedHashSet<>());
        }
        lists.get(count + 1).add((K) key);
        return vals.get(key);
    }

    /**
     * Удаляет элемент с указанным ключом из кэша.
     *
     * @param key Ключ элемента.
     * @return Значение удаленного элемента или null, если ключ не найден.
     */
    @Override
    public V remove(Object key) {
        // Удалить ключ из всех структур данных
        V value = vals.remove(key);
        Integer count = counts.remove(key);
        if (count != null) {
            lists.get(count).remove(key);
            if (lists.get(count).isEmpty()) {
                lists.remove(count);
            }
        }
        return value;
    }
}
