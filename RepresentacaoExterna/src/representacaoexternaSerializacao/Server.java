/*
 *  Learning Project.
 */
package representacaoexternaSerializacao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class Server {

    public static void main(String[] args) throws ClassNotFoundException {
        Gerenciamento g = null;

        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server running....");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                g = (Gerenciamento) in.readObject();
                System.out.println("Lido: " + g);
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
