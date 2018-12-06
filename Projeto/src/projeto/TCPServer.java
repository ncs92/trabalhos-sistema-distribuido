package projeto;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class Usuario {

    boolean jogando = false;
    String nome = "";
    int pontos = 0;
    int acertos = 0;
    boolean podeJogar = false;
    Socket socket;

    public Usuario() {

    }

    public Usuario(Socket s) {
        this.socket = s;
    }

    @Override
    public String toString() {
        return "Usuario{" + "jogando=" + jogando + ", nome=" + nome + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.jogando != other.jogando) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }

        return true;
    }

}

public class TCPServer {

    static ArrayList<String> ranking = new ArrayList();
    static ArrayList<JogoClass> jogos = new ArrayList();
    static ArrayList<Usuario> usuario = new ArrayList();

    public static void main(String args[]) {
        ranking.add("Lula 5 min");
        ranking.add("Maria 8 min");
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                LerMensagemCliente ler = new LerMensagemCliente(clientSocket);

                /* inicializa a thread */
                ler.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

class LerMensagemCliente extends Thread {

    DataInputStream in;
    DataOutputStream out;
    ObjectOutputStream objOut;
    Usuario u = new Usuario();
    JogoClass jogo = new JogoClass();
    ArrayList<String> fotos = new ArrayList<String>();

    public LerMensagemCliente(Socket socket) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.u.socket = socket;
    }
    
      public int escolheFoto() {
        Random gerador = new Random();
        while (true) {
            int num = gerador.nextInt(36);
            if (jogo.escolherAleatorio.get(num) != -1) {
                int v = jogo.escolherAleatorio.get(num);
                jogo.escolherAleatorio.set(num, -1);
                return v;
            }
        }
    }
    
    public void iniciaJogo(){
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 19; j++) {
                jogo.escolherAleatorio.add(j);
            }
        }
        
         for (int i = 1; i < 19; i++) {
            fotos.add(i + ".png");
        }
         
          for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                jogo.mr[i][j] = 0;
                JButton jb = new JButton();
                jb.setSize(64, 64);
                jb.setMinimumSize(new Dimension(64, 64));
                jogo.mb[i][j] = jb;
                jogo.mb[i][j].setIcon(new ImageIcon(getClass().getResource("./../img/question.png")));
                int num = escolheFoto() - 1;
                jogo.caminhoImagens[i][j] = "./../img/" + fotos.get(num);
               
              
              
            }
        }
    }

    public void run() {
        Scanner reader = new Scanner(System.in);
        String buffer = "";
        String retorno = "";
        Calendar calendar = Calendar.getInstance();

        try {
            while (true) {
                buffer = in.readUTF();
                System.out.println("\nMensagem recebida: " + buffer);
                if (buffer.equals("ranking")) {
                    objOut.writeObject(TCPServer.ranking);
                } else if (buffer.equals("play")) {

                    u.nome = buffer.split("|")[1];
                    if (TCPServer.usuario.contains(u)) {
                        out.writeUTF("contem");
                    } else {
                        TCPServer.usuario.add(u);
                        boolean achou = false;
                        for (Usuario user : TCPServer.usuario) {
                            if (!user.equals(u) && user.jogando == false) {
                                achou = true;
                                JogoClass jogo = new JogoClass();
                                u.jogando = true;
                                user.jogando = true;
                                u.podeJogar = true;
                                jogo.p1 = u;
                                jogo.p2 = user;

                                new ObjectOutputStream(jogo.p1.socket.getOutputStream())
                                        .writeObject(jogo);
                                new ObjectOutputStream(jogo.p2.socket.getOutputStream())
                                        .writeObject(jogo);

                            }
                        }
                        if(achou == false){
                            out.writeUTF("esperando");
                        }
                    }

                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                this.u.socket.close();
                in.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}
