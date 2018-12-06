package server;

import java.net.Socket;

public class Usuario {

    public boolean jogando = false;
    public String nome;
    public int erros = 0;
    public int acertos = 0;
    public Socket socket;
}