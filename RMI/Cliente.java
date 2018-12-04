/**
 * Solicita o servico
 * Autoras: Letícia Mazzo e Elaine Sangali
 * Data de Criação: 27/10/2018
 * Data de Modificação: 03/12/2018
 */

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

 public class Cliente {
     public static void main(String args[]) {
         try {
             System.out.println ("Cliente iniciado ...");

             if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
             } //if

            /* obtem a referencia para o objeto remoto */
            Registry registry = LocateRegistry.getRegistry("localhost");
            Gerencia c = (Gerencia)registry.lookup("ServicoLivro");
            
            
            ArrayList<String> livros = new ArrayList<String>();
            livros = c.adicionar(livros, "Garota Exemplar - Gillian Flynn");
            livros = c.adicionar(livros, "Lexico - Max Barry");
            livros = c.adicionar(livros, "Biblioteca de Almas - Ransom Riggs");

            c.listar(livros);

            livros = c.excluir(livros, "Lexico - Max Barry"); //Remove Lexico

            c.listar(livros);
         } catch (Exception e) {
            System.out.println(e);
         } //catch

     } //main
 } //Cliente