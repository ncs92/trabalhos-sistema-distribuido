package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramClientWorker extends Thread {
    
    private DatagramSocket socket;
    private ClientListener listener;

    public DatagramClientWorker(DatagramSocket socket, ClientListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            while (true) { magic(); }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void magic() throws IOException {
        byte[] buffer = new byte[1024];
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        if (!listener.onPacketReceived(packet)) {
            return;
        }
        
        String content = new String(packet.getData(), 0, packet.getLength())
                .trim();
        
        System.out.println("Received message: " + content);
        
        
        if (content.startsWith("JOINACK")) {
            String nick = extractJoinAckNick(content);
            listener.onJoinAckReceived(packet.getAddress(), packet.getPort(), nick);
        }
    }
    
    private String extractJoinAckNick(String content) {
        String[] parts = content.split("\\|");
        return (parts.length == 2) ? parts[1] : null;
    }
}
