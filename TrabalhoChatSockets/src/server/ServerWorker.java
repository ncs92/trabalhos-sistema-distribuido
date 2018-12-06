package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerWorker implements Runnable {
    
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    
    public ServerWorker(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
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
        String content = in.readUTF();
        
        System.out.println("Received message: " + content.trim());
        
        if (content.startsWith("hello")) {
            out.writeUTF("hello");
        }
        
        
    }
}
