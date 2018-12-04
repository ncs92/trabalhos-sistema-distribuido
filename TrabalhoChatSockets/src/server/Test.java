package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Test {
    
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        // InetAddress group = InetAddress.getByName("225.1.2.3");
        InetAddress group = InetAddress.getByName("127.0.0.1");
        byte[] buffer = "JOINACK|Elaine".getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 6789);
        socket.send(packet);
        socket.close();
    }
}
