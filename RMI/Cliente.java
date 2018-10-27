/**
 * Solicita o servico
 * Autoras: Letícia Mazzo e Elaine Sangali
 * data: 27/10/2018
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
	        Gerencia c = (Gerencia)registry.lookup("ServicoCalculadora");
            ArrayList<String> livros = c.adicionar("Garota Exemplar - Gillian Flynn");
            c.listar(livros);
         } catch (Exception e) {
            System.out.println(e);
         } //catch

     } //main
 } //Cliente