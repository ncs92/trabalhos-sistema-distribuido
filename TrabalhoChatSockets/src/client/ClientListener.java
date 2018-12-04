package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface ClientListener {
    
    boolean onPacketReceived(DatagramPacket packet);
    void onJoinAckReceived(InetAddress address, int port, String nick);
}
