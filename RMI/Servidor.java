/**
 * Inicializa o servico
 * Autoras: Letícia Mazzo e Elaine Sangali
 * Data de Criação: 27/10/2018
 * Data de Modificação: 03/12/2018
 */

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Servidor {
     public static void main(String args[]) {
       try {
             if (System.getSecurityManager() == null) {
                 System.setSecurityManager(new SecurityManager());
             }

             /* inicializa um objeto remoto */
             Gerencia c = new Gerenciamento();

             /* registra o objeto remoto no Binder */
             Registry registry = LocateRegistry.getRegistry("localhost");
	         registry.bind("ServicoLivro", c);

	         /* aguardando invocacoes remotas */
	         System.out.println("Servidor pronto ...");
	     } catch (Exception e) {
	         System.out.println(e);
         } //catch
     } //main
} //Servidor