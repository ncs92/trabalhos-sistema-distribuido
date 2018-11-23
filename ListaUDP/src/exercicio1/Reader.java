package exercicio1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Reader implements Runnable {
    
    private final DatagramSocket socket;
    private final Listener listener;

    public Reader(DatagramSocket socket, Listener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            read();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private String obtemParteBuffer(byte[] src, int pos, int tamanho) {
        byte[] dest = new byte[tamanho];
        System.arraycopy(src, pos, dest, 0, tamanho);
        
        return new String(dest, 0, tamanho);
    }
    
    private void read() throws IOException {
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacote);
            

            String nick = obtemParteBuffer(buffer, 0, 200).trim();
            String cmd = obtemParteBuffer(buffer, 201, 200).trim();
            
            if ("ADDNICK".equals(cmd)) {
                listener.onAddNickReceived(nick);
                continue;    
            }
            
            if ("MSG".equals(cmd)) {
                String msg = obtemParteBuffer(buffer, 402, 600).trim();
                listener.onMessageReceived(nick, msg);
            }
        }
    }
    
    public static interface Listener {
        void onAddNickReceived(String nick);
        void onMessageReceived(String nick, String msg);
    }
}
