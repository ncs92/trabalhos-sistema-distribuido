/*
 *  Learning Project.
 */
package representacaoexternaSerializacao;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

  
public class Client {
    public static void main(String[] args) {
        try {
            Gerenciamento g = new Gerenciamento("Toy Store", "Nilson", "leticia", 1997, 70, "a1235", 80, new Dimensao(5,15,30), "evolve", "12/01/1997");
            Socket clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 5555);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(g);
            out.flush();
            System.out.println("Client sends Object.");
            clientSocket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
