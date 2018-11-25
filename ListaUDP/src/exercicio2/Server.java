package exercicio2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
    
    private static final int PORT = 5555;
    
    private boolean running = true;
    private DatagramSocket socket;
    
    public static void main(String[] args) throws SocketException {
        Server server = new Server();
        new Thread(server).start();
    }

    public Server() throws SocketException {
        socket = new DatagramSocket(PORT);
        System.out.println("Server listening on port " + PORT);
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                this.magic();
            } catch (Exception ex) {
                System.err.println(ex);
                this.running = false;
            }
        }
    }
    
    private void magic() throws IOException {
        System.out.println("Waiting for data...");
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        
        this.onPacketReceived(packet);
    }
    
    private void onPacketReceived(DatagramPacket receivedPacket) throws IOException {
        byte[] data = receivedPacket.getData();
        String content = new String(data);
        System.out.println("Received packet: " + content);
        
        this.sendResponsePacket(receivedPacket);
    }
    
    private void sendResponsePacket(DatagramPacket receivedPacket) throws IOException {
        String contentReceived = new String(receivedPacket.getData());
        byte[] data = ("I received: " + contentReceived).getBytes();
        DatagramPacket packetToSend = new DatagramPacket(
                data,
                data.length,
                receivedPacket.getAddress(),
                receivedPacket.getPort()
        );
        
        socket.send(packetToSend);
    }
}
