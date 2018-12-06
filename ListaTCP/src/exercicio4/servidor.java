package exe4;

import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class servidor {
    static AtomicInteger palpite = new AtomicInteger();
  
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
                ClientThread c = new ClientThread(clientSocket, palpite);
                /* inicializa a thread */
                c.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    AtomicInteger palpite;
    Random rand = new Random();

    public ClientThread(Socket clientSocket, AtomicInteger palpite) {
        try {
            this.clientSocket = clientSocket;
            this.palpite = palpite;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            String buffer = "";
            palpite.set(rand.nextInt(1000) + 1);
            while (true) {
                buffer = in.readUTF();   /* aguarda o envio de dados */
                
                System.out.println(palpite);
                //buffer = in.readUTF();   /* aguarda o envio de dados */
                if(Integer.parseInt(buffer) == palpite.get()){
                    System.out.println("Acertaram o palpite!");
                    out.writeUTF("Acertou!");
                    palpite.set(rand.nextInt(1000) + 1);
                }
                if (buffer.equals("PARAR"))
                    break;

                out.writeUTF(buffer);
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class
