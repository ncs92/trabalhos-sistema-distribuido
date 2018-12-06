package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import model.DirectMessage;
import model.MulticastMessage;

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
        
        System.out.println("UDP message received: " + content);
        
        if (content.startsWith("JOINACK")) {
            String nick = extractOnlyNick(content);
            listener.onJoinAckReceived(packet.getAddress(), packet.getPort(), nick);
            
        } else if (content.startsWith("JOIN")) {
            String nick = extractOnlyNick(content);
            listener.onJoinReceived(packet.getAddress(), packet.getPort(), nick);
            
        } else if (content.startsWith("MSGIDV FROM")) {
            DirectMessage message = extractDirectMessage(content);
            listener.onMessageReceived(message.from, message.text, true);
            
        } else if (content.startsWith("MSG")) {
            MulticastMessage message = extractMulticastMessage(content);
            listener.onMessageReceived(message.from, message.text, false);
            
        } else if (content.startsWith("LEAVE")) {
            String nick = extractOnlyNick(content);
            listener.onLeaveReceived(nick);
        }
    }
    
    private String extractOnlyNick(String content) {
        String[] parts = content.split("\\|");
        return (parts.length == 2) ? parts[1] : null;
    }
    
    private DirectMessage extractDirectMessage(String content) {
        // MSGIDV FROM|Elaine|TO|Leticia|Oi, Leticia! Vamos no cinema hoje?
        String[] parts = content.split("\\|");
        if (parts.length < 5) {
            return null;
        }
        
        DirectMessage message = new DirectMessage();
        message.from = parts[1];
        message.to = parts[3];
        message.text = parts[4];
        
        return message;
    }
    
    private MulticastMessage extractMulticastMessage(String content) {
        // MSG|Elaine|Ola, pessoal!
        String[] parts = content.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        
        MulticastMessage message = new MulticastMessage();
        message.from = parts[1];
        message.text = parts[2];
        
        return message;
    }
}
