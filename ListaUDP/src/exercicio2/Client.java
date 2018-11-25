package exercicio2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Client implements Runnable {
    
    private static final int PORT = 5555;
    
    private boolean running = true;
    private DatagramSocket socket;
    
    public static void main(String[] args) throws SocketException {
        Client client = new Client();
        new Thread(client).start();
    }

    public Client() throws SocketException {
        socket = new DatagramSocket();
        System.out.println("Client started on port " + PORT);
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                this.magic();
                this.running = false;
            } catch (Exception ex) {
                System.err.println(ex);
                this.running = false;
            }
        }
    }
    
    private void magic() throws IOException, FileNotFoundException {
        
        
        
        
//        FileInputStream file = new FileInputStream();
//        byte[] ab = new byte[1024];
//        int available = file.available();
//        System.out.println("Total available: " + file.available());
//        
//        int read = file.read(ab);
//        
//        System.out.println("Total read: " + read);
//        System.out.println("Total available: " + file.available());
        
//        InetAddress address = InetAddress.getByName("localhost");
//        
//        byte[] buf = "Edvaldo Szymonek".getBytes();
//        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
//        socket.send(packet);
//        System.out.println("Packet sent");
//        
//        this.waitForResponsePacket();
    }
    
    private void sendFileInfoPacket(DatagramSocket socket, File file) throws FileNotFoundException {
        
        String checksum = FileUtils.getFileChecksum(file, "MD5");
        int size = (int) file.length();
        
        String message = "FILE" + checksum + String.valueOf(size);
        
//        byte[] message = new byte[32];
        
//        bytes[] length = Integer.parseInt("10");
//        
//        File file = new File("/home/edvaldo/Pictures/edvaldo.jpeg");
//        String checksum = FileUtils.getFileChecksum(file, "MD5");
        
        
//        DatagramPacket packet = new DatagramPacket()
    }
    
    
    
    
    
    private void waitForResponsePacket() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        
        socket.receive(packet);
        this.onPacketReceived(packet);
    }
    
    private void onPacketReceived(DatagramPacket packet) throws IOException {
        String content = new String(packet.getData());
        System.out.println("Cliente received content: " + content);
    }
}
