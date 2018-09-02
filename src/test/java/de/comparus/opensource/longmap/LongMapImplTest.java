package de.comparus.opensource.longmap;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

public class LongMapImplTest {
    @Test
    public void testInitializingLongMap() {
        LongMap<String> longMap = new LongMapImpl<>();
        assertNotNull(longMap.keys());
        assertNotNull(longMap.values());
        assertEquals(longMap.size(), 0);
        assertTrue(longMap.isEmpty());
    }


    @Test
    public void mapShouldContainEntry() {
        LongMap<Double> longMap = new LongMapImpl<>();
        longMap.put(33, 42.);
        assertTrue(longMap.containsKey(33));
        assertTrue(longMap.containsValue(42.));
    }

    @Test
    public void testGetValues() {
        LongMap<String> longMap = new LongMapImpl<>();
        longMap.put(123, "BestValue");
        assertEquals("BestValue", longMap.get(123));
    }

    @Test
    public void mapShouldResizeAndContainOldEntries() {
        LongMap<String> longMap = new LongMapImpl<>();
        int beforeResize = longMap.keys().length;
        for (int i = 0; i < beforeResize; i++) {
            longMap.put(i, "String" + i);
        }
        long[] oldKeys = longMap.keys();
        longMap.put(42, "Now Resize");
        assertEquals(beforeResize + 1, longMap.size());
        for (long key : oldKeys) {
            assertTrue(longMap.containsKey(key));
        }
        assertTrue(longMap.containsKey(42));
    }


    @Test
    public void testRemoveEntry() {
        LongMap<Double> longMap = new LongMapImpl<>();
        long key = 35;
        double value = 19.1923111333;
        longMap.put(key, value);
        double oldValue = longMap.remove(key);
        assertEquals(oldValue, value, 0.00000001);
        assertFalse(longMap.containsValue(value));
        assertFalse(longMap.containsKey(key));
    }

    @Test
    public void testRemoveEntryFromTheMiddle() {
        LongMap<String> longMap = new LongMapImpl<>();
        long key = 3;
        String value = "String3";

        longMap.put(1, "String1");
        longMap.put(2, "String2");
        longMap.put(3, "String3");
        longMap.put(4, "String4");
        longMap.put(5, "String5");

        long sizeBeforeRemove = longMap.size();

        String oldValue = longMap.remove(key);

        assertTrue(oldValue.equals(value));
        assertFalse(longMap.containsValue(value));
        assertFalse(longMap.containsKey(key));
        assertEquals(sizeBeforeRemove - 1, longMap.size());
        assertArrayEquals(new long[]{1, 2, 4, 5}, longMap.keys());
    }

    @Test
    public void testClear() {
        LongMap<Double> longMap = new LongMapImpl<>();
        longMap.put(22, 35.);
        longMap.put(141, 3231.2);
        longMap.clear();
        assertEquals(0, longMap.size());
        assertEquals(0, longMap.keys().length);
        assertEquals(0, longMap.values().length);
        assertTrue(longMap.isEmpty());
    }

    @Test
    public void memoryTest() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        LongMap<Double> longMap = new LongMapImpl<>();
        longMap.put(33L, 42.);
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryConsumedByLongMap = memoryAfter - memoryBefore;

        System.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        Map<Long, Double> hashMap = new HashMap<>();
        hashMap.put(33L, 42.);
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryConsumedByHashMap = memoryAfter - memoryBefore;

        assertTrue(memoryConsumedByLongMap < memoryConsumedByHashMap);

    }

    @Test
    public void memoryTest2() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String str = "Test";
        LongMap<String> longMap = new LongMapImpl<>();
        LongStream.range(1, 10)
                .forEach(key -> longMap.put(key, str + key));
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryConsumedByLongMap = memoryAfter - memoryBefore;

        System.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        Map<Long, String> hashMap = new HashMap<>();
        LongStream.range(1, 10)
                .forEach(key -> hashMap.put(key, str + key));
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryConsumedByHashMap = memoryAfter - memoryBefore;

        assertTrue(memoryConsumedByLongMap < memoryConsumedByHashMap * 2);
    }

    @Test
    public void perfomanceTest() {
        String str = "Test";
        long timeBefore = System.currentTimeMillis();
        LongMap<String> longMap = new LongMapImpl<>();
        LongStream.range(1, 100)
                .forEach(key -> longMap.put(key, str + key));

        long timeAfter = System.currentTimeMillis();
        long timeConsumedByLongMap = timeAfter - timeBefore;

        assertTrue(timeConsumedByLongMap < 150);

    }
}
