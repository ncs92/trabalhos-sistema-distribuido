package server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Serializable {

    private static final long serialVersionUID = 1L;

    ServerSocket socket;

    public Usuario jogador1;
    public Usuario jogador2;
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
        while (true) {
            System.out.println("Servidor aguardando conexões...");
            Socket cliente = socket.accept();
            if(jogador1 == null){
                socketJogador1 = cliente;
            }else{
                socketJogador2 = cliente;
            }

            new LeitorMensagemCliente(this, cliente)
                    .start();
        }

    }
}
