package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    
    ServerSocket socket;
    
    public Usuario jogador1;
    public Usuario jogador2;
    
    public Jogo jogo;
    
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor(8090);
            servidor.iniciar();
        } catch (IOException ex) {
            System.err.println("Falha ao iniciar o servidor");
            ex.printStackTrace();
        }
        
        
    }
    
    public Servidor(int porta) throws IOException {
        socket = new ServerSocket(porta);
    }
    
    private void iniciar() throws IOException {
        System.out.println("Servidor aguardando conexões...");
        Socket cliente = socket.accept();
        
        new LeitorMensagemCliente(this, cliente)
                .start();
        
        new EnviarJogo(this, cliente)
                .start();
    }
}
