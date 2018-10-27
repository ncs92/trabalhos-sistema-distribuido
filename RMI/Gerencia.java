/**
 * Define a interface para um gerencimaneto de musicas/livros remoto
 * Autoras: Letícia Mazzo e Elaine Sangali
 * Data: 27/10/2018
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Gerencia extends Remote {
    public ArrayList<String> adicionar (String livro) throws RemoteException;
    public void listar (ArrayList<String> livros) throws RemoteException;
    public ArrayList<String> excluir (ArrayList<String> livros) throws RemoteException;
}