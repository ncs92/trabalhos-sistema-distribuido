/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author Elaine
 */
public class Mensagem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public String texto;
    
    
    public Mensagem() {
        
    }
    
    public Mensagem(String texto) {
        this.texto = texto;
    }
}
