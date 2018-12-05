package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Test {
    
    public static void main(String[] args) throws IOException {
        DatagramSocket datagram = new DatagramSocket();
        
        sendJoinAck(datagram, "Elaine");
        // sendMulticastMessage(datagram);
        // sendDirectMessage(datagram);
        // sendLeave(datagram, "Elaine");
        
        datagram.close();
    }
    
    public static void sendJoinAck(DatagramSocket socket, String nick) throws IOException {
        InetAddress group = InetAddress.getByName("127.0.0.1");
        byte[] buffer = ("JOINACK|" + nick).getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 6789);
        socket.send(packet);
    }
    
    public static void sendMulticastMessage(DatagramSocket socket) throws IOException {
        InetAddress group = InetAddress.getByName("225.1.2.3");
        byte[] buffer = "MSG|Elaine| Ola, pessoal!".getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 6789);
        socket.send(packet);
    }
    
    public static void sendDirectMessage(DatagramSocket socket) throws IOException {
        InetAddress address = InetAddress.getByName("127.0.0.1");
        byte[] buffer = "MSGIDV FROM|Elaine|TO|Leticia|Oi, Leticia! Vamos no cinema hoje?".getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 6789);
        socket.send(packet);
    }
    
    public static void sendLeave(DatagramSocket socket, String nick) throws IOException {
        InetAddress address = InetAddress.getByName("225.1.2.3");
        byte[] buffer = ("LEAVE|" + nick).getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 6789);
        socket.send(packet);
    }
}
