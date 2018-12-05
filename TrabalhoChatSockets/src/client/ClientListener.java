package client;

import java.net.DatagramPacket;
import java.net.InetAddress;

public interface ClientListener {
    
    boolean onPacketReceived(DatagramPacket packet);
    
    void onJoinReceived(InetAddress address, int port, String nick);
    void onJoinAckReceived(InetAddress address, int port, String nick);
    
    void onMessageReceived(String from, String text, boolean direct);
    
    void onLeaveReceived(String nick);
}
