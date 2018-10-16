package exercicio5;

/**
 * TCPClient: Cliente para conexao TCP Descricao: Envia uma informacao ao
 * servidor e recebe confirmações ECHO Ao enviar "PARAR", a conexão é
 * finalizada.
 */
import exercicio2.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {
    
    private OnMensagemRecebida listener;
    public Socket clientSocket = null;

    public OnMensagemRecebida getListener() {
        return listener;
    }

    public void setListener(OnMensagemRecebida listener) {
        this.listener = listener;
    }
    
    public void connect() throws IOException{
      
        Scanner reader = new Scanner(System.in); // ler mensagens via teclado

        try {
            /* Endereço e porta do servidor */
            int serverPort = 6666;
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");

            /* conecta com o servidor */
            clientSocket = new Socket(serverAddr, serverPort);

            LerMensagem ler = new LerMensagem(clientSocket, this.listener);

            ler.start();
            
        } catch (UnknownHostException ue) {
            System.out.println("Socket:" + ue.getMessage());
        } 
    }
 

    public static void main(String args[]) throws IOException {
        
    } //main

    public interface OnMensagemRecebida {

        public void onMensagemRecebida(byte[] mensagem);

    }
}



