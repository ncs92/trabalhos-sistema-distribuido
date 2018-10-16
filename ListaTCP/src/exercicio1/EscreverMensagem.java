/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercicio1;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Elaine
 */
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
                System.out.println("Mensagem: ");
                buffer = reader.nextLine();
                out.writeUTF(buffer);
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
                out.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}


