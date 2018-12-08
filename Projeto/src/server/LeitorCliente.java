package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LeitorCliente implements Runnable {
    
    private Servidor servidor;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public LeitorCliente(Servidor servidor, Socket socket) throws IOException {
        this.servidor = servidor;
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    private void iniciaJogo() throws IOException {
        Jogo jogo = new Jogo();
        
        jogo.jogador1 = servidor.jogador1;
        jogo.jogador2 = servidor.jogador2;
        jogo.jogador2.jogando = true;
        
        servidor.enviaObjetoJogoParaTodos(jogo);
        System.out.println("Iniciado novo jogo");
    }
    
    private void registraUsuario(Mensagem mensagem) throws IOException {
        String nome = mensagem.texto.split("\\|")[1];
        
        Usuario usuario = new Usuario(nome);
        
        boolean euSouJogador1 = servidor.jogador1 != null && servidor.jogador1.nome.equals(nome);
        boolean euSouJogador2 = servidor.jogador2 != null && servidor.jogador2.nome.equals(nome);
        if (euSouJogador1 || euSouJogador2) {
            System.out.println("O jogador " + nome + " ja esta registrado no servidor");
            out.writeObject(new Mensagem("contem"));
            return;
        }
        
        if (servidor.jogador1 == null) {
            servidor.jogador1 = usuario;
            servidor.out1 = out;
            System.out.println("Registrado jogador 1");
            
            if (servidor.jogador2 == null) {
                out.writeObject(new Mensagem("esperando"));
            } else {
                iniciaJogo();
            }
            
        } else if (servidor.jogador2 == null) {
            servidor.jogador2 = usuario;
            servidor.out2 = out;
            System.out.println("Registrado jogador 2");
            
            iniciaJogo();
            
        } else {
            out.writeObject(new Mensagem("esperando"));
        }
    }
    
    private void ler() throws IOException, ClassNotFoundException {
        Object objeto = in.readObject();
        System.out.println("Novo objeto recebido");
        
        if (objeto instanceof Mensagem) {
            System.out.println("Suponho que seja um registro de usuario");
            Mensagem mensagem = (Mensagem) objeto;
            registraUsuario(mensagem);
            
        } else if (objeto instanceof Jogo) {
            Jogo jogo = (Jogo) objeto;
            
            jogo.jogador1.jogando = !jogo.jogador1.jogando;
            jogo.jogador2.jogando = !jogo.jogador2.jogando;
            
            servidor.enviaObjetoJogoParaTodos(jogo);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ler();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
