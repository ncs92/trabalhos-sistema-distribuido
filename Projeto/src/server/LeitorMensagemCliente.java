package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class LeitorMensagemCliente extends Thread {

    private Servidor servidor;
    private final Usuario usuario = new Usuario();

    ObjectOutputStream out;
    ObjectInputStream in;
    // ObjectOutputStream objOut;
    Usuario u = new Usuario();

    public LeitorMensagemCliente(Servidor servidor, Socket cliente) throws IOException {
        this.servidor = servidor;

        this.out = new ObjectOutputStream(cliente.getOutputStream());
        this.in = new ObjectInputStream(cliente.getInputStream());

    }

    private boolean euExistoNoServidor(String nome) {
        if (servidor.jogador1 == null && servidor.jogador2 == null) {
            return false;
        } else {
            boolean euSouJogador2 = servidor.jogador1 == null && servidor.jogador2.nome.equals(nome);
            boolean euSouJogador1 = servidor.jogador2 == null && servidor.jogador1.nome.equals(nome);

            return euSouJogador1 || euSouJogador2;
        }

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
        jogo.jogador2.jogando = true;
        servidor.jogo = jogo;

        return servidor.jogo;
    }

    public static void enviarObjetoJogo(Socket socket, Jogo jogo, ObjectOutputStream out) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(jogo);
        } catch (IOException ex) {
            System.err.println("Falha ao enviar o jogo para os clientes");
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object recebido = in.readObject();
                System.out.println("Objeto recebido" + recebido);

                if (recebido instanceof Mensagem) {
                    Mensagem men = (Mensagem) recebido;
                    System.out.println("Mensagem recebida pelo servidor: " + men.texto);
                    String buffer = String.valueOf(men.texto);

                    if (buffer.startsWith("play")) {
                        String[] palavras = buffer.split(";");
                        String nome = palavras[1];
                        if (euExistoNoServidor(nome)) {
                            System.out.println("EU EXISTO");
                            men.texto = "contem";
                            out.writeObject(men);

                        } else if (existeOutroJogador()) {
                            System.out.println("INICIAR JOGO");
                            Jogo jogo = iniciaNovoJogo(nome);
                            enviarObjetoJogo(servidor.socketJogador1, jogo, this.out);
                            enviarObjetoJogo(servidor.socketJogador2, jogo, this.out);

                        } else {
                            men.texto = "esperando";
                            usuario.nome = nome;
                            servidor.jogador1 = usuario;
                            out.writeObject(men);
                        }

                    } else if (buffer.equals("ranking")) {
                        System.out.println("Tem que implementar o ranking");
                    }

                } else {
                    System.out.println("Entrou jogo");
                    Jogo jog = (Jogo) recebido;
                    enviarObjetoJogo(servidor.socketJogador1, jog, this.out);
                    enviarObjetoJogo(servidor.socketJogador2, jog, this.out);
                }
            }
        } catch (Exception ex) {
            System.err.println("Falha durante a conexão");
            ex.printStackTrace();
        } finally {
            finalizaConexao();
        }
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
