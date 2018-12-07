package multicastsockettest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSocketTest {

    public static void main(String[] args) throws IOException {
        MulticastSocket socket = new MulticastSocket(9090);
        
        InetAddress group = InetAddress.getByName("225.1.2.3");
        socket.joinGroup(group);
        
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            socket.receive(packet);
            
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received message: " + message);
        }
    }
}
