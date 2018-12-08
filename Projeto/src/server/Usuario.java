package server;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean jogando = false;
    public String nome;
    public int erros = 0;
    public int acertos = 0;
   
}
