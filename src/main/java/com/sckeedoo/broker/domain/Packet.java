package com.sckeedoo.broker.domain;

import java.io.Serializable;

public class Packet<T> implements Serializable {
    private T object;
    private String query;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
