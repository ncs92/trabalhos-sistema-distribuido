package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClientWorker extends Thread {
    
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientListener listener;
    
    public SocketClientWorker(Socket socket, ClientListener listener) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            this.out.writeUTF("hello");
            while (true) { magic(); }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void magic() throws IOException {
        String content = in.readUTF();
        
        System.out.println("TPC message received: " + content.trim());
    }
}
