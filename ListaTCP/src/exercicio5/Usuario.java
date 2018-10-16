/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercicio5;

import exercicio2.*;
import java.net.Socket;

/**
 *
 * @author Elaine
 */
public class Usuario {
    Socket socket;

    public Usuario(String nome, Socket socket) {
        this.socket = socket;
    }
    
    
}
