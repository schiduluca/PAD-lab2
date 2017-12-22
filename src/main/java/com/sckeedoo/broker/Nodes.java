package com.sckeedoo.broker;

import com.sckeedoo.broker.domain.Node;
import com.sckeedoo.broker.domain.University;

import java.io.IOException;
import java.util.Arrays;

public class Nodes {

    public static void main(String[] args) throws IOException {

        Node nodeOne = new Node("One",4);
        Node nodeTwo = new Node("Two",3);
        Node nodeThree = new Node("Three",1);
        University universityOne = new University();
        universityOne.setCountry("Moldova");
        universityOne.setRating(10);
        universityOne.setName("UTM");
        universityOne.setStudentList(Arrays.asList("Victor", "Vasile", "Luca", "Dragos", "Nicu"));

        University universityTwo = new University();
        universityTwo.setCountry("Moldova");
        universityTwo.setRating(7);
        universityTwo.setName("USM");
        universityTwo.setStudentList(Arrays.asList("Ion", "Gicu", "Grigore", "Vadim"));

        University universityThree = new University();
        universityThree.setCountry("Moldova");
        universityThree.setRating(6);
        universityThree.setName("ASEM");
        universityThree.setStudentList(Arrays.asList("Alexandru", "Ionela", "Valeria", "Victoria", "Gheorghie"));

        ClientNode clientNodeOne = new ClientNode(nodeOne, 8090, universityOne);
        ClientNode clientNodeTwo = new ClientNode(nodeTwo, 8091, universityTwo);
        ClientNode clientNodeThree = new ClientNode(nodeThree, 8092, universityThree);
        clientNodeOne.start();
        clientNodeTwo.start();
        clientNodeThree.start();



    }
}
