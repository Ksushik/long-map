package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LongMapImpl<V> implements LongMap<V> {

    private long[] keys;
    private V[] values;
    int size;

    private static final int DEFAULT_CAPACITY = 0;

    public LongMapImpl() {
        this(DEFAULT_CAPACITY);
    }

    public LongMapImpl(int initialCapacity) {
        this.keys = new long[initialCapacity];
        this.values = (V[]) new Object[initialCapacity];
        this.size = 0;
    }

    public V put(long key, V value) {
        int position = Arrays.binarySearch(keys, key);
        if (position >= 0) {
            V oldValue = values[position];
            values[position] = value;
            return oldValue;
        }
        position = -position - 1; // binarySearch method returns index of the search key, if it is contained in the array;
        // otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.
        keys = Arrays.copyOf(keys, size + 1);
        values = Arrays.copyOf(values, size + 1);

        if (position < size) {
            System.arraycopy(keys, position, keys, position + 1, size - (position + 1));
            System.arraycopy(values, position, values, position + 1, size - (position + 1));
        }
        keys[position] = key;
        values[position] = value;

        size++;

        return value;
    }

    public V get(long key) {
        int position = Arrays.binarySearch(keys, key);
        return position > -1 ? values[position] : null;
    }

    public V remove(long key) {
        if (size == 0) {
            return null;
        }

        int position = Arrays.binarySearch(keys, key);

        if (position > -1) {
            V oldValue = values[position];
            System.arraycopy(keys, position + 1, keys, position, size - (position + 1));
            System.arraycopy(values, position + 1, values, position, size - (position + 1));
            values[--size] = null;
            keys = Arrays.copyOf(keys, size);
            values = Arrays.copyOf(values, size);
            return oldValue;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        return Arrays.binarySearch(keys, key) > -1;
    }

    public boolean containsValue(V value) {
        return Arrays.binarySearch(values, value) > -1;
    }

    public long[] keys() {
        return this.keys;
    }

    public V[] values() {
        return this.values;
    }

    public long size() {
        return this.size;
    }

    public void clear() {
        this.keys = new long[DEFAULT_CAPACITY];
        this.values = (V[]) Array.newInstance(values[0].getClass(), DEFAULT_CAPACITY);
        this.size = 0;
    }
}
