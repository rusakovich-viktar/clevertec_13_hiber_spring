package by.clevertec.house.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LfuCacheTest {

    private LfuCache<String, Integer> cache;

    @BeforeEach
    void setUp() {
        cache = new LfuCache<>(2);
    }

    @Test
    void testPutAndGet() {
        cache.put("one", 1);
        assertEquals(1, cache.get("one"));
        assertNull(cache.get("two"));
    }

    @Test
    void testPutWithZeroCapacity() {
        cache = new LfuCache<>(0);
        assertNull(cache.put("one", 1));
    }

    @Test
    void testPutWithExistingKey() {
        cache.put("one", 1);
        assertEquals(2, cache.put("one", 2));
        assertEquals(2, cache.get("one"));
    }

    @Test
    void testPutWithFullCache() {
        cache.put("one", 1);
        cache.put("two", 2);
        cache.put("three", 3);

        assertNull(cache.get("one"));
        assertEquals(2, cache.get("two"));
        assertEquals(3, cache.get("three"));
    }

    @Test
    void testPutWithNonFullCache() {
        cache.put("one", 1);
        assertEquals(1, cache.get("one"));
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
