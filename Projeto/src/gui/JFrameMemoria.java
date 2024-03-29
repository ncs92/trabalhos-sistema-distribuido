/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import server.Jogo;
import server.Mensagem;

/**
 *
 * @author Elaine
 */
public final class JFrameMemoria extends javax.swing.JFrame implements Runnable {

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 6365;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nome;
    private boolean jogando = false;
    private int acertos = 0;
    private int erros = 0;

    private boolean iniciado = false;
    private Jogo jogo;

    private JButton[][] mb = new JButton[6][6];

    ArrayList<String> fotos = new ArrayList<String>();

    int cliques = 0, i_prim = 0, j_prim = 0, i_sec = 0, j_sec = 0;
    static String meuNome = "";
    // EscreverMensagemObjeto escrever;

    /**
     * Creates new form JFrameVelha
     */
    public void setAcerto() {
        this.jLabelP1Acertos.setText(String.valueOf(jogo.jogador1.acertos));
        this.jLabelP2Acertos.setText(String.valueOf(jogo.jogador2.acertos));

    }

    public void setErro() {
        System.out.println("EROOOOOO" + jogo.jogador1.equals(jogo.jogador2));
        if (jogo.jogador1.nome.equals(this.nome)) {
            jogo.jogador1.erros += 1;
        } else {
            jogo.jogador2.erros += 1;
        }
        this.jLabelP1Erros.setText(String.valueOf(jogo.jogador1.erros));
        this.jLabelP2Erros.setText(String.valueOf(jogo.jogador2.erros));

    }

    public void selecionaButton(int i, int j) {
        if ((jogo.jogador1.nome.equals(this.nome) && jogo.jogador1.jogando == true)
                || jogo.jogador2.nome.equals(this.nome) && jogo.jogador2.jogando == true) {
            mb[i][j].setEnabled(true);
        }

        cliques++;
        if (cliques == 1) {
            i_prim = i;
            j_prim = j;
            mb[i][j].setIcon(new ImageIcon(getClass().getResource(jogo.ci[i][j])));

        } else if (cliques == 2) {
            i_sec = i;
            j_sec = j;
            mb[i][j].setIcon(new ImageIcon(getClass().getResource(jogo.ci[i][j])));

            if (jogo.ci[i_prim][j_prim].equals(jogo.ci[i_sec][j_sec])) {
                if (jogo.jogador1.nome.equals(this.nome)) {
                    jogo.jogador1.acertos += 1;
                } else {
                    jogo.jogador2.acertos += 1;
                }

                setAcerto();
                jogo.mr[i_prim][j_prim] = 1;
                jogo.mr[i_sec][j_sec] = 1;
            } else {
                setErro();
            }

            atualizaInterface();
            cliques = 0;
        }
    }

    private void ocultaIcones() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (jogo.mr[i][j] == 0) {
                    mb[i][j].setIcon(new ImageIcon(getClass().getResource("./../img/question.png")));
                }
            }
        }
    }

    private void atualizaInterface() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    ocultaIcones();
                    out.writeObject(jogo);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };

        new Thread(r).start();
    }

    private void iniciaNovoJogo(Jogo jogo) {
        jPanel1.setLayout(new GridLayout(6, 6));

        jPanel1.setSize(32767, 32767);
        //jPanel1.setMinimumSize(new Dimension(32767, 32767));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                final int i1 = i;
                final int j1 = j;
                mb[i][j] = new JButton(new ImageIcon(getClass().getResource("./../img/question.png")));
                if (jogo.jogador1 == null || jogo.jogador2 == null) {
                    mb[i][j].setEnabled(false);
                } else if ((jogo.jogador1.nome.equals(this.nome) && jogo.jogador1.jogando == true)
                        || jogo.jogador2.nome.equals(this.nome) && jogo.jogador2.jogando == true) {
                    mb[i][j].setEnabled(true);

                }

                mb[i][j].addActionListener((ActionEvent ae) -> {
                    selecionaButton(i1, j1);
                });

                if (this.nome.equals(jogo.jogador1.nome)) {
                    mb[i][j].setEnabled(jogo.jogador1.jogando);

                } else if (this.nome.equals(jogo.jogador2.nome)) {
                    mb[i][j].setEnabled(jogo.jogador2.jogando);
                }

                jPanel1.add(mb[i][j]);
            }
        }

        if (jogo.jogador1 != null && jogo.jogador2 != null) {
            this.jLabelP1Acertos.setText(String.valueOf(jogo.jogador1.acertos));
            this.jLabelP1Erros.setText(String.valueOf(jogo.jogador1.erros));

            this.jLabelP2Acertos.setText(String.valueOf(jogo.jogador2.acertos));
            this.jLabelP2Erros.setText(String.valueOf(jogo.jogador2.erros));
        }
    }

    private void atualizaJogo(Jogo jogo) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                final int i1 = i;
                final int j1 = j;

                if (this.nome.equals(jogo.jogador1.nome)) {
                    mb[i][j].setEnabled(jogo.jogador1.jogando);

                } else if (this.nome.equals(jogo.jogador2.nome)) {
                    mb[i][j].setEnabled(jogo.jogador2.jogando);
                }

                if (jogo.mr[i][j] == 1) {
                    mb[i][j].setIcon(new ImageIcon(getClass().getResource(jogo.ci[i][j])));
                    mb[i][j].setEnabled(false);
                } else {
                    mb[i][j].setIcon(new ImageIcon(getClass().getResource("./../img/question.png")));
                }
            }
        }

        if (jogo.jogador1 != null && jogo.jogador2 != null) {
            this.jLabelP1Acertos.setText(String.valueOf(jogo.jogador1.acertos));
            this.jLabelP1Erros.setText(String.valueOf(jogo.jogador1.erros));

            this.jLabelP2Acertos.setText(String.valueOf(jogo.jogador2.acertos));
            this.jLabelP2Erros.setText(String.valueOf(jogo.jogador2.erros));
        }
    }

    public JFrameMemoria() throws IOException {
        initComponents();

        socket = new Socket(InetAddress.getByName(SERVER_HOST), SERVER_PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        new Thread(this).start();

        ImageIcon icon = new ImageIcon(getClass().getResource("./../img/doc.png"));
        this.jLabel1.setIcon(icon);
        ImageIcon icon2 = new ImageIcon(getClass().getResource("./../img/marty.png"));

        this.jLabel2.setIcon(icon2);

        this.jLabelP1ImgResultado.setVisible(false);
        this.jLabelP2ImgResultado.setVisible(false);
        this.jLabelP1TextoResultado.setVisible(false);
        this.jLabelP2TextoResultado.setVisible(false);
//        geraNovoJogo();
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

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
        jLabelP1Acertos.setText("0");

        jLabelP1Erros.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP1Erros.setForeground(new java.awt.Color(255, 51, 51));
        jLabelP1Erros.setText("0");

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
                .addContainerGap(15, Short.MAX_VALUE))
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
        jLabelP2Acertos.setText("0");

        jLabelP2Erros.setFont(new java.awt.Font("Alef", 1, 12)); // NOI18N
        jLabelP2Erros.setForeground(new java.awt.Color(255, 51, 51));
        jLabelP2Erros.setText("0");

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
                .addContainerGap(15, Short.MAX_VALUE))
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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        while (true) {
            String nome = JOptionPane.showInputDialog(null, "Informe seu nome");
            if (nome != null && !"".equals(nome)) {
                this.nome = nome;
                break;
            }
        }

        try {
            registraUsuario();
            System.out.println("Registrando usuario...");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_formWindowOpened

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
                    ex.printStackTrace();
                }
            }
        });
    }

    private void registraUsuario() throws IOException, ClassNotFoundException {
        Mensagem me = new Mensagem("play|" + this.nome);
        out.writeObject(me);
    }

    private void ler() throws IOException, ClassNotFoundException {
        Object objeto = in.readObject();
        System.out.println("Novo objeto recebido");

        if (objeto instanceof Mensagem) {
            // Mensagem mensagem = (Mensagem) objeto;
            JOptionPane.showMessageDialog(null, "Aguardando outro jogador...");

        } else if (objeto instanceof Jogo) {
            Jogo jogo = (Jogo) objeto;
            System.out.println(jogo.acabou);
            System.out.println(jogo.jogador1);
            System.out.println(jogo.jogador2);

            this.jogo = jogo;

            if (!iniciado) {
                iniciaNovoJogo(this.jogo);
                iniciado = true;
            } else {
                atualizaJogo(this.jogo);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ler();
            }

        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
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
