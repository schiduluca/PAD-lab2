package com.sckeedoo.broker;

import com.sckeedoo.broker.domain.Node;
import com.sckeedoo.broker.domain.Packet;
import com.sckeedoo.broker.domain.University;
import com.sckeedoo.broker.domain.converter.GsonConverter;
import com.sckeedoo.broker.domain.converter.XmlConverter;
import com.sckeedoo.broker.protocol.TCPConnection;
import com.sckeedoo.broker.protocol.Connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class QueryListenerService extends Thread {
    private ServerSocket serverSocket = new ServerSocket(8100);
    private Socket accept;
    private Map<String, Node> nodeMap = new ConcurrentHashMap<>();
    private Socket nodeSocket;
    private Connection connection;

    public QueryListenerService(Map<String, Node> nodeMap) throws IOException {
        this.nodeMap = nodeMap;
        initNodeConnections(nodeMap);
        System.out.println("Query Service started. Listening for queries...");
    }

    @Override
    public void run() {
        try {
            accept = serverSocket.accept();
            connection = new TCPConnection(accept);
            while(accept.isConnected()) {
                String query = connection.read();
                String[] split = query.split("\\?");
                System.out.println(query);
                Packet<String> packet = new Packet<>();
                String collect = aggregateData(split[0]).stream().map(e -> {
                    if(split.length == 1 || split[1].contains("xml")) {
                        return XmlConverter.convertToXMLString(e, University.class);
                    } else {
                        return GsonConverter.convertToJson(e);
                    }
                }).collect(Collectors.joining());
                packet.setObject(collect);
                connection.write(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<University> aggregateData(String query) {
        List<University> list = new ArrayList<>();
        nodeMap.values().forEach(node -> {
            node.getConnection().write(query);
            String university = node.getConnection().read();
            list.add(GsonConverter.convertToDto(university, University.class));
        });
        return list;
    }




    private void initNodeConnections(Map<String, Node> nodeMap) {
        nodeMap.values().forEach(node -> {
            try {
                nodeSocket = new Socket("localhost", node.getPort());
                node.setConnection(new TCPConnection(nodeSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
