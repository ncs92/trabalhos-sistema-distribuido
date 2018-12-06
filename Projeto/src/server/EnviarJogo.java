package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class EnviarJogo extends Thread {

    private Servidor servidor;

    ObjectInputStream in;
    ObjectOutputStream out;

    public EnviarJogo(Servidor servidor, Socket cliente) throws IOException {
        this.servidor = servidor;

        this.in = new ObjectInputStream(cliente.getInputStream());
        this.out = new ObjectOutputStream(cliente.getOutputStream());
    }

    public static void enviarObjetoJogo(Socket socket, Jogo jogo) {
        try {
            new ObjectOutputStream(socket.getOutputStream())
                    .writeObject(jogo);
        } catch (IOException ex) {
            System.err.println("Falha ao enviar o jogo para os clientes");
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ler();
            }
        } catch (Exception ex) {
            System.err.println("Falha durante a conexão");
            ex.printStackTrace();
        } finally {
            finalizaConexao();
        }
    }

    private void ler() throws IOException, ClassNotFoundException {
        Jogo jog = (Jogo) in.readObject();
        enviarObjetoJogo(servidor.jogador1.socket, jog);
        enviarObjetoJogo(servidor.jogador2.socket, jog);

    }

    private void finalizaConexao() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            System.err.println("Falha ao fechar a conexão");
            ex.printStackTrace();
        }
    }
}
