/*
 *  Learning Project.
 */
package representacaoexternaBuffer;

import representacaoexternaSerializacao.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import representacaoexternaBuffer.GerenciamentoOuterClass.Gerenciamento;
import representacaoexternaBuffer.GerenciamentoOuterClass.Dimensao;

  
public class Client {
    public static void main(String[] args) {
        try {
            Dimensao.Builder dbuilder = Dimensao.newBuilder();
            Dimensao dimensao = dbuilder.setAltura(2)
                                        .setLargura(3)
                                        .setComprimento(4).build();
                    
            Gerenciamento.Builder builder = Gerenciamento.newBuilder();
            Gerenciamento g = builder.setTitulo("Capitao ao mar")
                    .setAutor("Jack")
                    .setCantor("Leonardo")
                    .setAno(2010)
                    .setNumPagina(5)
                    .setIsbn("a123")
                    .setPeso(10)
                    .setDim(dimensao)
                    .setAlbum("lorion")
                    .setDataLancamento("01/05/2017").build();
            
            Socket clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 5555);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.write(g.toByteArray());
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
