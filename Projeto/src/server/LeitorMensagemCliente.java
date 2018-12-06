package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static server.EnviarJogo.enviarObjetoJogo;

class LeitorMensagemCliente extends Thread {

    private Servidor servidor;
    private final Usuario usuario;

    ObjectInputStream in;
    ObjectOutputStream out;
    // ObjectOutputStream objOut;
    Usuario u = new Usuario();

    public LeitorMensagemCliente(Servidor servidor, Socket cliente) throws IOException {
        this.servidor = servidor;

        this.in = new ObjectInputStream(cliente.getInputStream());
        this.out = new ObjectOutputStream(cliente.getOutputStream());
        // this.objOut = new ObjectOutputStream(cliente.getOutputStream());
        this.u.socket = cliente;

        usuario = new Usuario();
        usuario.socket = cliente;
    }

    private boolean euExistoNoServidor(String nome) {
        boolean euSouJogador1 = servidor.jogador1 != null && servidor.jogador1.nome.equals(nome);
        boolean euSouJogador2 = servidor.jogador2 != null && servidor.jogador2.nome.equals(nome);

        return euSouJogador1 || euSouJogador2;
    }

    private boolean existeOutroJogador() {
        return servidor.jogador1 != null || servidor.jogador2 != null;
    }

    private Jogo iniciaNovoJogo(String nome) {
        Usuario u = new Usuario();
        u.nome = nome;
        u.jogando = true;

        if (servidor.jogador1 == null) {
            servidor.jogador1 = u;
        } else {
            servidor.jogador2 = u;
        }

        Jogo jogo = new Jogo();
        jogo.jogador1 = servidor.jogador1;
        jogo.jogador2 = servidor.jogador2;
        servidor.jogo = jogo;

        return servidor.jogo;
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
        Object recebido = in.readObject();
        if (recebido instanceof Mensagem) {
            Mensagem men = (Mensagem) recebido;
            String buffer = men.texto;

            if (buffer.startsWith("play")) {
                String nome = buffer.split("|")[1];
                if (euExistoNoServidor(nome)) {
                    out.writeUTF("contem");

                } else if (existeOutroJogador()) {
                    Jogo jogo = iniciaNovoJogo(nome);
                    enviarObjetoJogo(jogo.jogador1.socket, jogo);
                    enviarObjetoJogo(jogo.jogador2.socket, jogo);

                } else {
                    System.out.println("esperando");
                    usuario.nome = nome;
                    out.writeUTF("esperando");
                }

            } else if (buffer.equals("ranking")) {
                System.out.println("Tem que implementar o ranking");
            }

        } else {
            Jogo jog = (Jogo) recebido;
            enviarObjetoJogo(servidor.jogador1.socket, jog);
            enviarObjetoJogo(servidor.jogador2.socket, jog);
        }

    }

    private void finalizaConexao() {
        try {
            usuario.socket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            System.err.println("Falha ao fechar a conexão");
            ex.printStackTrace();
        }
    }

    public void EnviarJogo(Servidor servidor, Socket cliente) throws IOException {
        this.servidor = servidor;

        this.in = new ObjectInputStream(cliente.getInputStream());
        this.out = new ObjectOutputStream(cliente.getOutputStream());
    }

}
