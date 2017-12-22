package com.sckeedoo.broker;


import com.sckeedoo.broker.domain.Node;
import com.sckeedoo.broker.domain.Packet;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private Map<String, Node> nodeMap = new ConcurrentHashMap<>();

    public void startServer() {
        try {
            new DiscoveryService().start();
            registerUsers();
            new QueryListenerService(nodeMap).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerUsers() throws IOException {
        DatagramPacket packet;
        DatagramSocket datagramSocket = new DatagramSocket(4001);
        datagramSocket.setSoTimeout(2000);
        while(!datagramSocket.isClosed()) {
            try {
                byte[] buf = new byte[2048];
                packet = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(packet);
                Packet<Node> received = SerializationUtils.deserialize(packet.getData());
                System.out.println(packet.getSocketAddress());
                System.out.println("Registration request received from : " + received.getObject().getName() + ", saving...");
                received.getObject().setSocketAddress(packet.getSocketAddress());
                nodeMap.put(received.getObject().getName(), received.getObject());
            } catch (SocketTimeoutException e) {
                System.out.println("Registration finished");
                break;
            }
        }
    }


}
