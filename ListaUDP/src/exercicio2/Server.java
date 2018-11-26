package exercicio2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Runnable {
    
    private static final int PORT = 5555;
    
    private boolean running = true;
    private DatagramSocket socket;
    
    private String checksum;
    private int size;
    private String name;
    
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
        try {
            this.magic();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void magic() throws IOException {
        System.out.println("Waiting for file info data...");
//        byte[] buf = new byte[1024];
//        DatagramPacket packet = new DatagramPacket(buf, buf.length);
//        socket.receive(packet);
        
        this.waitForFileInfoReceived(socket);
    }
    
    private void waitForFileInfoReceived(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[142];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        
        byte[] message = packet.getData();
        
        byte[] checksumRaw = new byte[32];
        DataUtils.copyPartFromArray(message, 0, checksumRaw);
        String checksumLocal = new String(checksumRaw);
        this.checksum = checksumLocal.trim();
        
        byte[] sizeRaw = new byte[10];
        DataUtils.copyPartFromArray(message, 32, sizeRaw);
        String sizeLocal = new String(sizeRaw);
        this.size = Integer.parseInt(sizeLocal.trim());
        
        byte[] nameRaw = new byte[100];
        DataUtils.copyPartFromArray(message, 42, nameRaw);
        String nameLocal = new String(nameRaw);
        this.name = nameLocal.trim();
        
        System.out.println("Checksum: " + this.checksum);
        System.out.println("File size: " + this.size);
        System.out.println("File name: " + this.name);
        
        sendOkResponsePacket(socket, packet.getAddress(), packet.getPort());
        
        startReceivingFile(socket, name, size);
    }
    
    private void sendOkResponsePacket(DatagramSocket socket, InetAddress address, int port) throws IOException {
        byte[] data = "OK".getBytes();
        DatagramPacket packetToSend = new DatagramPacket(data, data.length, address, port);
        
        System.out.println("File info accepted. Sending OK to client...");
        socket.send(packetToSend);
    }
    
    private void startReceivingFile(DatagramSocket socket, String name, int size) throws FileNotFoundException, IOException {
        File file = new File("/home/edvaldo/Music/" + name);
        FileOutputStream fos = new FileOutputStream(file);
        
        int parts = (int) Math.ceil(size / 1024.0);
        System.out.println("parts: " + parts);
        
        int count = 1;
        while (count <= parts) {
            
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            
            byte[] chunk = packet.getData();
            System.out.println("Receiving file: " + (count * 100 / parts) + "%");
            
            fos.write(chunk);
            
            sendOkCountPacket(socket, packet.getAddress(), packet.getPort());
            
            count++;
        }
        
        fos.close();
        
        String localFileChecksum = FileUtils.getFileChecksum(file, "MD5");
        if (this.checksum.equals(localFileChecksum)) {
            System.out.println("The local checksum is equal to remote checksum");
        } else {
            System.out.println("The local checksum is different to remote checksum. Try to send the file again.");
        }
    }
    
    private void sendOkCountPacket(DatagramSocket socket, InetAddress address, int port) throws IOException {
        byte[] chunk = "OK".getBytes();
        DatagramPacket packet = new DatagramPacket(chunk, chunk.length, address, port);
        
        socket.send(packet);
    }
}
