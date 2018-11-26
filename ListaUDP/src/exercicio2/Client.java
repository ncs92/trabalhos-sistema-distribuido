package exercicio2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable {
    
    private static final int PORT = 5555;
    
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
        try {
            this.magic();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void magic() throws IOException, FileNotFoundException {
        File file = new File("/home/edvaldo/Pictures/edvaldo.jpeg");
        
        sendFileInfoPacket(socket, file);
        waitForOkPacket(socket);
        startSendingFile(socket, file);
        
        System.out.println("File upload finished.");
    }
    
    private void sendFileInfoPacket(DatagramSocket socket, File file) throws FileNotFoundException, UnknownHostException, IOException {
        
        String checksum = FileUtils.getFileChecksum(file, "MD5");
        int size = (int) file.length();
        
        System.out.println(file.getName());
        
        byte[] message = new byte[142];
        DataUtils.copyPartToArray(checksum.getBytes(), message, 0);
        DataUtils.copyPartToArray(String.valueOf(size).getBytes(), message, 32);
        DataUtils.copyPartToArray(file.getName().getBytes(), message, 43);
        
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(message, message.length, address, PORT);
        socket.send(packet);
    }
    
    private void waitForOkPacket(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[142];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        
        String message = new String(packet.getData(), 0, packet.getLength());
        if ("OK".equals(message.trim())) {
            System.out.println("File accepted. Starting upload...");
        } else {
            System.err.println("File not accepted. Upload finished.");
            System.exit(0);
        }
    }
    
    private void startSendingFile(DatagramSocket socket, File file) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(file);
        InetAddress address = InetAddress.getByName("localhost");
        
        int size = (int) file.length();
        int parts = (int) Math.ceil(size / 1024.0);
        
        System.out.println("size: " + size);
        System.out.println("parts: " + parts);
        System.out.println("available: " + fis.available());
        
        int count = 1;
        while (count <= parts) {
            
            byte[] chunk = new byte[1024];
            int read = fis.read(chunk);
            
            if (read < 0) {
                System.out.println("File end reached.");
                break;
            }
            
            DatagramPacket packet = new DatagramPacket(chunk, chunk.length, address, PORT);
            socket.send(packet);
            
            if (!waitForServerPartResponse(socket)) {
                // Volta no loop e reenvia o pedaço
                continue;
            }
            
            System.out.println("Sending file: " + (count * 100 / parts) + "%");
            
            count++;
        }
    }
    
    private boolean waitForServerPartResponse(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[2];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        
        String message = new String(packet.getData(), 0, packet.getLength());
        return "OK".equals(message.trim());
    }
}
