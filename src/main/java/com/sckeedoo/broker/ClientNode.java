package com.sckeedoo.broker;

import com.sckeedoo.broker.domain.Node;
import com.sckeedoo.broker.domain.Packet;
import com.sckeedoo.broker.domain.ProtocolConfig;
import com.sckeedoo.broker.domain.University;
import com.sckeedoo.broker.domain.converter.GsonConverter;
import com.sckeedoo.broker.domain.converter.XmlConverter;
import com.sckeedoo.broker.protocol.Connection;
import com.sckeedoo.broker.protocol.TCPConnection;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientNode extends Thread {
    private Node node;
    private MulticastSocket socket;
    private InetAddress group;
    private DatagramSocket communicationSocket;
    private University university;
    private ServerSocket server;
    private Connection connection;


    public ClientNode(Node node, int port, University university) {
        this.node = node;
        this.node.setPort(port);
        this.university = university;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Node started: " + node.getName());
        startClientNode();
    }

    private void startClientNode() {
        try {
            socket = new MulticastSocket(ProtocolConfig.PROTOCOL_GROUP_PORT);
            group = InetAddress.getByName(ProtocolConfig.PROTOCOL_GROUP_ADDRESS);
            socket.joinGroup(group);
            communicationSocket = new DatagramSocket();
            listenForConnectionRequests();
            startProducer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForConnectionRequests() throws IOException {
        DatagramPacket packet;
        byte[] buf = new byte[2048];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        SerializationUtils.deserialize(packet.getData());
        System.out.println("Broadcast message received");
        System.out.println("Sending request for node " + node.getName() + "... ");
        Packet<Node> sentPacked = new Packet<>();
        sentPacked.setObject(node);
        byte[] bytes = SerializationUtils.serialize(sentPacked);
        DatagramPacket sentPacket = new DatagramPacket(bytes, bytes.length, packet.getAddress(), 4001);
        communicationSocket.send(sentPacket);
        System.out.println("Request sent.");
        socket.leaveGroup(group);
        socket.close();
    }

    private void startProducer() {
        try {
            Socket accept = server.accept();
            connection = new TCPConnection(accept);
            while (true) {
                String query  = connection.read();
                University clone = cloneUniversity(query);
                connection.write(GsonConverter.convertToJson(clone));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private University cloneUniversity(String query) {
        University clone = null;
        try {
            clone = (University) university.clone();
            clone.setStudentList(university.getStudentList()
                    .stream()
                    .filter(name -> name.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

