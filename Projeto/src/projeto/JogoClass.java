/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author Elaine
 */
public class JogoClass {

    Integer[][] mr = new Integer[6][6];
    ArrayList<Integer> escolherAleatorio = new ArrayList();
    int p1Pontos = 0;
    int p2Pontos = 0;
    int p1Erros = 0;
    int p2Erros = 0;
    Usuario p1 = new Usuario();
    Usuario p2 = new Usuario();
    boolean acabou = false;

    JButton[][] mb = new JButton[6][6];
    String[][] caminhoImagens = new String[6][6];

    public JogoClass() {
    }

}
