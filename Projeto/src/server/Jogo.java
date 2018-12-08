package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;

public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;

    ArrayList<String> fotos = new ArrayList<>();
    public List<Integer> escolherAleatorio = new ArrayList<>();

    public int[][] mr = new int[6][6]; // matriz resultado
    public String[][] ci = new String[6][6]; // ci = caminho imagem

    public boolean acabou = false;
    public Usuario jogador1;
    public Usuario jogador2;

    public Jogo() {
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 19; j++) {
                escolherAleatorio.add(j);
            }
        }

        for (int i = 1; i < 19; i++) {
            fotos.add(i + ".png");
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int num = escolheFoto() - 1;
                ci[i][j] = "./../img/" + fotos.get(num);
                final int i1 = i;
                final int j1 = j;

            }
        }

        Collections.shuffle(fotos);
    }

    public int escolheFoto() {
        Random gerador = new Random();
        while (true) {
            int num = gerador.nextInt(36);
            if (escolherAleatorio.get(num) != -1) {
                int v = escolherAleatorio.get(num);
                escolherAleatorio.set(num, -1);
                return v;
            }
        }
    }
}
