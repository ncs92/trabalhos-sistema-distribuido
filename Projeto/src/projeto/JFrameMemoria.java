/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Elaine
 */
public class JFrameMemoria extends javax.swing.JFrame {

    ArrayList<String> fotos = new ArrayList<String>();
    static JogoClass jogo = new JogoClass();
    int cliques = 0, i_prim = 0, j_prim = 0, i_sec = 0, j_sec = 0;

    /**
     * Creates new form JFrameVelha
     */
    public int escolheFoto() {
        Random gerador = new Random();
        while (true) {
            int num = gerador.nextInt(36);
            if (jogo.escolherAleatorio.get(num) != -1) {
                int v = jogo.escolherAleatorio.get(num);
                jogo.escolherAleatorio.set(num, -1);
                return v;
            }
        }
    }

    public void setAcerto() {
        this.jLabelP1Acertos.setText(String.valueOf(jogo.p1Pontos));
    }

    public void setErro() {
        this.jLabelP1Erros.setText(String.valueOf(jogo.p1Erros));
    }

    public void selecionaButton(int i, int j) throws InterruptedException {
        cliques++;
        if (cliques == 1) {
            i_prim = i;
            j_prim = j;
            jogo.mb[i][j].setIcon(new ImageIcon(getClass().getResource(jogo.caminhoImagens[i][j])));

        } else if (cliques == 2) {
            i_sec = i;
            j_sec = j;
            jogo.mb[i][j].setIcon(new ImageIcon(getClass().getResource(jogo.caminhoImagens[i][j])));

            if (jogo.caminhoImagens[i_prim][j_prim].equals(jogo.caminhoImagens[i_sec][j_sec])) {
                jogo.p1Pontos += 1;
                setAcerto();
                jogo.mr[i_prim][j_prim] = 1;
                jogo.mr[i_sec][j_sec] = 1;
            } else {
                setErro();
                jogo.p1Erros += 1;
            }
            jogo.podeJogar = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JFrameMemoria.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    enviaAoServidor();
                    ocultaIcones();
                }
            }).start();
            cliques = 0;
        }
    }

    public void enviaAoServidor() {

    }

    public void ocultaIcones() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (jogo.mr[i][j] == 0) {
                    jogo.mb[i][j].setIcon(new ImageIcon(getClass().getResource("./../img/question.png")));
                    jogo.mb[i][j].setEnabled(false);
                }
            }
        }
    }

    public void iniciaServidor() throws IOException {
        Socket clientSocket = null; // socket do cliente

        try {
            /* Endereço e porta do servidor */
            int serverPort = 6666;
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");

            /* conecta com o servidor */
            clientSocket = new Socket(serverAddr, serverPort);

            LerMensagem ler = new LerMensagem(clientSocket);
            EscreverMensagem escrever = new EscreverMensagem(clientSocket);

            escrever.start();
            ler.start();

        } catch (UnknownHostException ue) {
            System.out.println("Socket:" + ue.getMessage());
        }
    }

    public JFrameMemoria() throws IOException {
        initComponents();
        iniciaServidor();
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 19; j++) {
                jogo.escolherAleatorio.add(j);
            }
        }

        jPanel1.setLayout(new GridLayout(6, 6));

        for (int i = 1; i < 19; i++) {
            fotos.add(i + ".png");
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                jogo.mr[i][j] = 0;
                JButton jb = new JButton();
                jb.setSize(64, 64);
                jb.setMinimumSize(new Dimension(64, 64));
                jogo.mb[i][j] = jb;
                jogo.mb[i][j].setIcon(new ImageIcon(getClass().getResource("./../img/question.png")));
                int num = escolheFoto() - 1;
                jogo.caminhoImagens[i][j] = "./../img/" + fotos.get(num);
                final int i1 = i;
                final int j1 = j;
                jogo.mb[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            selecionaButton(i1, j1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(JFrameMemoria.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                jPanel1.add(jogo.mb[i][j]);
            }
        }

        jPanel1.setSize(32767, 32767);
        jPanel1.setMinimumSize(new Dimension(32767, 32767));

        ImageIcon icon = new ImageIcon(getClass().getResource("./../img/doc.png"));
        this.jLabel1.setIcon(icon);
        ImageIcon icon2 = new ImageIcon(getClass().getResource("./../img/marty.png"));
        this.jLabel2.setIcon(icon2);
        this.jLabelP1Acertos.setText(String.valueOf(jogo.p1Pontos));
        this.jLabelP2Acertos.setText(String.valueOf(jogo.p2Pontos));
        this.jLabelP1Erros.setText(String.valueOf(jogo.p1Erros));
        this.jLabelP2Erros.setText(String.valueOf(jogo.p2Erros));
        this.jLabelP1ImgResultado.setVisible(false);
        this.jLabelP2ImgResultado.setVisible(false);
        this.jLabelP1TextoResultado.setVisible(false);
        this.jLabelP2TextoResultado.setVisible(false);
        geraNovoJogo();
    }

    public void geraNovoJogo() {
        Collections.shuffle(fotos);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelP1Acertos = new javax.swing.JLabel();
        jLabelP1Erros = new javax.swing.JLabel();
        jLabelP1TextoResultado = new javax.swing.JLabel();
        jLabelP1ImgResultado = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelP2Acertos = new javax.swing.JLabel();
        jLabelP2Erros = new javax.swing.JLabel();
        jLabelP2TextoResultado = new javax.swing.JLabel();
        jLabelP2ImgResultado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 487));
        setSize(new java.awt.Dimension(800, 487));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(128, 128));

        jLabel1.setText("jLabel1");

        jLabel3.setFont(new java.awt.Font("Alef", 1, 14)); // NOI18N
        jLabel3.setText("Jogador 1");

        jLabel5.setFont(new java.awt.Font("Alef", 1, 10)); // NOI18N
        jLabel5.setText("Total de Acertos:");

        jLabel6.setFont(new java.awt.Font("Alef", 1, 10)); // NOI18N
        jLabel6.setText("Total de Erros:");

        jLabelP1Acertos.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP1Acertos.setForeground(new java.awt.Color(0, 204, 0));
        jLabelP1Acertos.setText("jLabel9");

        jLabelP1Erros.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP1Erros.setForeground(new java.awt.Color(255, 51, 51));
        jLabelP1Erros.setText("jLabel9");

        jLabelP1TextoResultado.setFont(new java.awt.Font("Alef", 1, 14)); // NOI18N
        jLabelP1TextoResultado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelP1TextoResultado.setText("Você Ganhou!!!");

        jLabelP1ImgResultado.setText("jLabel4");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelP1Acertos))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP1Erros))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelP1ImgResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelP1TextoResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabelP1Acertos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelP1Erros))
                .addGap(18, 18, 18)
                .addComponent(jLabelP1ImgResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelP1TextoResultado)
                .addGap(40, 40, 40))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(128, 128));

        jLabel2.setText("jLabel1");

        jLabel4.setFont(new java.awt.Font("Alef", 1, 14)); // NOI18N
        jLabel4.setText("Jogador 2");

        jLabel7.setFont(new java.awt.Font("Alef", 1, 10)); // NOI18N
        jLabel7.setText("Total de Acertos:");

        jLabel8.setFont(new java.awt.Font("Alef", 1, 10)); // NOI18N
        jLabel8.setText("Total de Erros:");

        jLabelP2Acertos.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP2Acertos.setForeground(new java.awt.Color(0, 204, 0));
        jLabelP2Acertos.setText("jLabel9");

        jLabelP2Erros.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP2Erros.setForeground(new java.awt.Color(255, 51, 51));
        jLabelP2Erros.setText("jLabel9");

        jLabelP2TextoResultado.setFont(new java.awt.Font("Alef", 1, 14)); // NOI18N
        jLabelP2TextoResultado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelP2TextoResultado.setText("Você Ganhou!!!");

        jLabelP2ImgResultado.setText("jLabel4");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelP2Acertos))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelP2Erros))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelP2ImgResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelP2TextoResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelP2Acertos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelP2Erros))
                .addGap(18, 18, 18)
                .addComponent(jLabelP2ImgResultado, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelP2TextoResultado)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMemoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMemoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMemoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMemoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JFrameMemoria().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(JFrameMemoria.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelP1Acertos;
    private javax.swing.JLabel jLabelP1Erros;
    private javax.swing.JLabel jLabelP1ImgResultado;
    private javax.swing.JLabel jLabelP1TextoResultado;
    private javax.swing.JLabel jLabelP2Acertos;
    private javax.swing.JLabel jLabelP2Erros;
    private javax.swing.JLabel jLabelP2ImgResultado;
    private javax.swing.JLabel jLabelP2TextoResultado;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}

class EscreverMensagem extends Thread {

    ObjectOutputStream out;
    Socket socket;

    public EscreverMensagem(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        
        try {
            while (true) {
                if (JFrameMemoria.jogo.podeJogar == false) {
                    out.writeObject(JFrameMemoria.jogo);
                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                socket.close();
                out.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}

class LerMensagem extends Thread {

    ObjectInputStream in;
    Socket socket;

    public LerMensagem(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        Scanner reader = new Scanner(System.in);

        try {
            while (true) {
                JFrameMemoria.jogo = (JogoClass) in.readObject();
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LerMensagem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
                in.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }

    }

}
