package exercicio3;

/**
 * TCPClient: Cliente para conexao TCP Descricao: Envia uma informacao ao
 * servidor e recebe confirmações ECHO Ao enviar "PARAR", a conexão é
 * finalizada.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String args[]) throws IOException {
        Socket clientSocket = null; // socket do cliente
        Scanner reader = new Scanner(System.in); // ler mensagens via teclado

        try {
            /* Endereço e porta do servidor */
            int serverPort = 6666;
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");

            /* conecta com o servidor */
            clientSocket = new Socket(serverAddr, serverPort);

            LerMensagem ler = new LerMensagem(clientSocket);
            EscreverMensagem escrever = new EscreverMensagem(clientSocket);

            escrever.start();
            ler.start();

        } catch (UnknownHostException ue) {
            System.out.println("Socket:" + ue.getMessage());
        }
    } //main
} //class

class EscreverMensagem extends Thread {

    DataOutputStream out;
    Socket socket;

    public EscreverMensagem(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        Scanner reader = new Scanner(System.in);
        String buffer = "";

        try {
            while (true) {
                buffer = reader.nextLine();
                out.writeUTF(buffer);
                if (buffer.equals("EXIT")) {
                    break;
                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                socket.close();
                out.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}

class LerMensagem extends Thread {

    DataInputStream in;
    Socket socket;

    public LerMensagem(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
    }

    public void run() {
        Scanner reader = new Scanner(System.in);
        String buffer = "";

        try {
            while (true) {
                buffer = in.readUTF();
                System.out.println("\nMensagem recebida: " + buffer);

                if (buffer.contains("tamArq")) {
                    long tamArq = Long.parseLong(buffer.split(" ")[1]);
                    byte[] bytesRecebidos = new byte[1024];
                    String nomeArq = buffer.split(" ")[2];
                    int bytesLidos;
                    FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Elaine\\Documents\\shared\\recebidos\\" + nomeArq), true);
                    do{
                        bytesLidos = in.read(bytesRecebidos, 0, bytesRecebidos.length);
                        fos.write(bytesRecebidos, 0, bytesRecebidos.length);
                    }
                    while (!(bytesLidos < 1024));
                    System.out.println("Transmissão concluída!\n");
                }

                if (buffer.equals("SAIR")) {
                    break;
                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                socket.close();
                in.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}
