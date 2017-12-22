package com.sckeedoo.broker;

import com.sckeedoo.broker.domain.Packet;
import com.sckeedoo.broker.protocol.Connection;
import com.sckeedoo.broker.protocol.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientCLI {

    private Scanner read = new Scanner(System.in);
    private Socket socket = new Socket("localhost", 8100);
    private Connection connection;


    public ClientCLI() throws IOException {
        connection = new TCPConnection(socket);
    }

    public static void main(String[] args) throws IOException {
        ClientCLI clientCLI = new ClientCLI();
        clientCLI.startCLI();
    }

    public void startCLI() throws IOException {
        while (true) {
            System.out.println("Enter filter value for students name, just press ENTER for getting all students:");
            String s = read.nextLine();
            connection.write(s);
            Packet<String> packet = connection.read();
            System.out.println(packet.getObject());
        }
    }
}
