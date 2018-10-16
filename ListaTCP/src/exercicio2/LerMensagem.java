/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercicio2;

import exercicio1.*;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Elaine
 */
class LerMensagem extends Thread {

    DataInputStream in;
    Socket socket;
    TCPClient.OnMensagemRecebida mensagem;
    
    public LerMensagem(Socket socket, TCPClient.OnMensagemRecebida mensagem) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.mensagem = mensagem;
    }

    public void run() {
        System.out.println("Esperando mensagem");
        String buffer = "";

        try {
            while (true) {
                buffer = in.readUTF();
                System.out.println("Mensagem recebida: " + buffer);
                
                this.mensagem.onMensagemRecebida(buffer);

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

