package exe4;

/**
 * TCPClient: Cliente para conexao TCP
 * Descricao: Envia uma informacao ao servidor e recebe confirmações ECHO
 * Ao enviar "PARAR", a conexão é finalizada.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class cliente {
	public static void main (String args[]) {
	    Socket clientSocket = null; // socket do cliente
            Scanner reader = new Scanner(System.in); // ler mensagens via teclado
        
            try{
                /* Endereço e porta do servidor */
                int serverPort = 6666;   
                InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
                
                /* conecta com o servidor */  
                clientSocket = new Socket(serverAddr, serverPort);  
                
                /* cria objetos de leitura e escrita */
                DataInputStream in = new DataInputStream( clientSocket.getInputStream());
                DataOutputStream out =new DataOutputStream( clientSocket.getOutputStream());
            
                /* protocolo de comunicação */
                String buffer = "";
               
                while (true) {
                    System.out.print("Palpite: ");
                    buffer = reader.nextLine(); // lê mensagem via teclado
                
                    out.writeUTF(buffer);      	// envia a mensagem para o servidor
		
                    if (buffer.equals("PARAR"))
                        break;
                    
                    buffer = in.readUTF();      // aguarda resposta do servidor
                    if(buffer.equals("Acertou!"))
                        System.out.println("Você acertou!!!");
                    else
                        System.out.println("Palpite Incorreto!!!");
                } 
	    } catch (UnknownHostException ue){
		System.out.println("Socket:" + ue.getMessage());
            } catch (EOFException eofe){
		System.out.println("EOF:" + eofe.getMessage());
            } catch (IOException ioe){
		System.out.println("IO:" + ioe.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ioe) {
                    System.out.println("IO: " + ioe);;
                }
            }
     } //main
} //class
