package com.wavy.app.restdata;

public class Pair<T, K> {

    private T param;
    private K value;

    public Pair(T param, K value) {
        this.param = param;
        this.value = value;
    }

    public T getParam() {
        return param;
    }

    public K getValue() {
        return value;
    }
}