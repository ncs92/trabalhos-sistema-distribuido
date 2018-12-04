/**
 * Implementacao do objeto remoto
 * Autoras: Letícia Mazzo e Elaine Sangali
 * Data de Criação: 27/10/2018
 * Data de Modificação: 03/12/2018
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Gerenciamento extends UnicastRemoteObject implements Gerencia {

    public Gerenciamento() throws RemoteException {
        super();
        System.out.println("Objeto remoto instanciado");
    }

    // public ArrayList<String> adicionar () throws RemoteException {
    //     ArrayList<String> livros = new ArrayList();
    //     livros.add("Lexico - Max Barry");
    //     livros.add("Biblioteca de Almas - Ransom Riggs");
    //     System.out.println("Livro adicionado com sucesso!");

    //     return livros;
    // }

    public ArrayList<String> adicionar (ArrayList<String> livros, String livro) throws RemoteException {
        livros.add(livro);
        System.out.println("Livro adicionado com sucesso!");

        return livros;
    }

    public void listar (ArrayList<String> livros) throws RemoteException {
        System.out.println("Lista de Livros: ");
        int n = livros.size();
        for(int i = 0; i < n; i++){
            System.out.printf("%d: %s\n", i, livros.get(i));
        }
    }

    public ArrayList<String> excluir (ArrayList<String> livros, String id) throws RemoteException {
        try {
            livros.remove(id);
            System.out.println("Livro removido com sucesso!\n");
        } catch (IndexOutOfBoundsException e) {
                System.out.printf("\nErro ao excluir: ID inválido (%s).",
                e.getMessage());
            }
        
          return livros;
    }
} 
