package com.sckeedoo.broker.protocol;

import java.io.Serializable;

public interface Connection {
    <T extends Serializable> void write(T object);
    <T extends Serializable> T read();
}
