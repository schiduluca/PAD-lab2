package com.sckeedoo.broker;

import com.sckeedoo.broker.domain.Packet;
import com.sckeedoo.broker.domain.ProtocolConfig;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DiscoveryService extends Thread {

    private DatagramSocket socket = new DatagramSocket();

    private int hops = 0;

    public DiscoveryService() throws SocketException {
    }

    @Override
    public void run() {
        while (hops < 5) {
            try {
                Packet customPacket = new Packet();
                byte[] serialize = SerializationUtils.serialize(customPacket);
                InetAddress group = InetAddress.getByName(ProtocolConfig.PROTOCOL_GROUP_ADDRESS);
                DatagramPacket packet;
                packet = new DatagramPacket(serialize, serialize.length, group, ProtocolConfig.PROTOCOL_GROUP_PORT);
                socket.send(packet);
                try {
                    System.out.println("Discovery package send");
                    Thread.sleep(500);
                    hops++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
