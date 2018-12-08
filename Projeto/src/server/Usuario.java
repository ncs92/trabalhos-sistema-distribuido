package server;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public String nome;
    public int erros = 0;
    public int acertos = 0;
    public boolean jogando = false;
    
    public Usuario(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome + ", " + jogando + ", " + acertos + ", " + erros;
    }
}
