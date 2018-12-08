package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Serializable {

    private static final long serialVersionUID = 1L;

    private ServerSocket socket;

    public Usuario jogador1;
    public ObjectOutputStream out1;
    
    public Usuario jogador2;
    public ObjectOutputStream out2;
    
    public Socket socketJogador1;
    public Socket socketJogador2;

    public Jogo jogo;

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor(6365);
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
        System.out.println("Servidor aguardando conexoes...");
        
        while (true) {
            Socket cliente = socket.accept();
            System.out.println("Novo cliente conectado");
            
            new Thread(new LeitorCliente(this, cliente)).start();
        }
    }
    
    public void enviaObjetoJogoParaTodos(Jogo jogo) throws IOException {
        out1.writeObject(jogo);
        out2.writeObject(jogo);
    }
}
