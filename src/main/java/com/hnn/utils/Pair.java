package com.hnn.utils;

import java.util.Arrays;

public class Pair {
    private String[] key;
    private Boolean value;

    public Pair(String[] key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    public String[] getKey() {
        return key;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + Arrays.toString(key) +
                ", value=" + value +
                '}';
    }
}
