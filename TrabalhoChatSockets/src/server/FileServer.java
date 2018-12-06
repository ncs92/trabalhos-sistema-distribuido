package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    
    private static final int PORT = 9090;
    
    private ServerSocket server;
    
    public static void main(String[] args) throws IOException {
        new FileServer(PORT).listen();
    }
    
    public FileServer(int port) throws IOException {
        server = new ServerSocket(port);
    }
    
    private void listen() throws IOException {
        System.out.println("Server is listening on port " + PORT);
        while (true) {
            Socket client = server.accept();
            ServerWorker worker = new ServerWorker(client);
            new Thread(worker).start();
        }
    }
}
