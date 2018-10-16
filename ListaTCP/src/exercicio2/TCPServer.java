package exercicio2;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    
    public static List<Usuario> usuarios = new ArrayList<Usuario>();

    public static void main(String args[]) {
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);
            

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();
                usuarios.add(new Usuario("", clientSocket));

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                LerMensagem ler = new LerMensagem(clientSocket, new TCPClient.OnMensagemRecebida() {
                    @Override
                    public void onMensagemRecebida(String mensagem) {
                        for(Usuario usuario : usuarios){ 
                            if(usuario.socket != clientSocket){
                                try {
                                    new DataOutputStream(usuario.socket.getOutputStream())
                                            .writeUTF(mensagem);
                                } catch (IOException ex) {
                                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                });
                
                ler.start();
              
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class