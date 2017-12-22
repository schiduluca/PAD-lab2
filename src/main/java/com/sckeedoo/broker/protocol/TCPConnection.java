package com.sckeedoo.broker.protocol;

import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class TCPConnection implements Connection {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private byte[] data = new byte[2048];

    public TCPConnection(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new DataInputStream(this.socket.getInputStream());
            outputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Serializable> void write(T object) {
        data = SerializationUtils.serialize(object);
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Serializable> T read() {
        data = new byte[2048];
        try {
            inputStream.read(data);
            return SerializationUtils.deserialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
