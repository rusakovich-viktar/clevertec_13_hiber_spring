package by.clevertec.house.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LruCacheTest {

    private LruCache<String, Integer> cache;

    @BeforeEach
    void setUp() {
        cache = new LruCache<>(2);
    }

    @Test
    void testPutAndGet() {
        cache.put("one", 1);
        assertEquals(1, cache.get("one"));
        assertNull(cache.get("two"));
    }

    @Test
    void testEviction() {
        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);

        assertNull(cache.get("one"));
        assertEquals(2, cache.get("two"));
        assertEquals(3, cache.get("three"));
    }

    @Test
    void testUpdate() {
        cache.put("one", 1);
        cache.put("two", 2);
        cache.get("one");
        cache.put("three", 3);

        assertEquals(1, cache.get("one"));
        assertNull(cache.get("two"));
        assertEquals(3, cache.get("three"));
    }

    @Test
    void testRemove() {
        cache.put("one", 1);
        cache.remove("one");

        assertNull(cache.get("one"));
    }
}
