package exercicio3;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class TCPServer {

    public static void main(String args[]) {
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
    Socket socket;

    public LerMensagemCliente(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
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
                if (buffer.equals("TIME")) {
                    retorno = String.valueOf(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
                    out.writeUTF(retorno);
                } else if (buffer.equals("DATE")) {
                    retorno = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
                    out.writeUTF(retorno);
                } else if (buffer.equals("FILES")) {
                    File file = new File("C:\\Users\\Elaine\\Documents\\shared");
                    File todosArquivos[] = file.listFiles();
                    String listaNomes = "";

                    for (int i = 0; i < todosArquivos.length; i++) {
                        File arquivos = todosArquivos[i];
                        listaNomes += arquivos.getName() + "\n";
                    }
                    out.writeUTF(listaNomes);
                } else if (buffer.contains("DOWN")) {
                    String nomeArq = buffer.split(" ")[1];

                    File arq = new File("C:\\Users\\Elaine\\Documents\\shared\\" + nomeArq);
                    out.writeUTF("tamArq " + arq.length() + " " + nomeArq);
                    FileInputStream in = new FileInputStream(arq);
                    byte[] bytesLidos = new byte[1024];
                    int qtdLida;

                    while ((qtdLida = in.read(bytesLidos)) != -1) {
                        out.write(bytesLidos, 0, qtdLida);
                    }
                    
                } else if (buffer.equals("EXIT")) {
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
