/**
 * Implementacao do objeto remoto
 * Autoras: Letícia Mazzo e Elaine Sangali
 * data: 27/10/2018
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

    public ArrayList<String> adicionar (String livro) throws RemoteException {
        ArrayList<String> livros = new ArrayList();
        livros.add(livro);
        System.out.println("Livro adicionado com sucesso!");

        return livros;
    }

    public void listar (ArrayList<String> livros) throws RemoteException {
        System.out.println("Lista de Livros: ");
        int n = livros.size();
        for(int i = 0; i < n; i++){
            System.out.println("%d: %s", i, livros.get(i));
        }
    }

    public void excluir (ArrayList<String> livros) throws RemoteException {
        System.out.println("Informe o ID do livro que deseja excluir: ");

        int n = livros.size();
        int i;

        for(i = 0; i < n; i++){
            System.out.println("%d: %s", i, livros.get(i));
        }

        i = ler.nextInt();

        try {
            
            livros.remove(i);
          } catch (IndexOutOfBoundsException e) {
              System.out.printf("\nErro: ID inválido (%s).",
                e.getMessage());
          }
    }
} 
