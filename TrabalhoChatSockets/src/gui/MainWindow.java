package gui;

import client.ClientListener;
import client.DatagramClientWorker;
import client.SocketClientWorker;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.User;

public class MainWindow extends javax.swing.JFrame implements ClientListener {
    
    private static final String UDP_ADDRESS = "127.0.0.1";
    private static final int DATAGRAM_PORT_SERVER = 6798;
    
    private static final String MULTICAST_ADDRESS = "225.1.2.3";
    private static final int MULTICAST_PORT_SERVER = 6788;
    private static final int MULTICAST_PORT_SEND = 6789;
    
    private static final String TCP_ADDRESS = "127.0.0.1";
    private static final int TCP_PORT = 9090;
    
    private MulticastSocket multicast;
    private DatagramSocket datagram;
    private Socket socket;
    
    private String nick;
    
    private DefaultListModel<User> users = new DefaultListModel<>();

    public MainWindow() {
        initComponents();
        
        try {
            users.addElement(createGroupUser());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        userList.setModel(users);
        userList.setSelectedIndex(0);
    }
    
    private User createGroupUser() throws UnknownHostException {
        User user = new User();
        user.address = InetAddress.getByName(MULTICAST_ADDRESS);
        user.port = MULTICAST_PORT_SEND;
        user.nick = "Todos do grupo";
        
        return user;
    }
    
    private int findUserIndexByNick(String nick) {
        for (int i = 0; i < users.getSize(); i++) {
            User user = users.get(i);
            if (user.nick.equals(nick)) {
                return i;
            }
        }
        
        return -1;
    }
    
    private User findUserByNick(String nick) {
        int index = findUserIndexByNick(nick);
        return index < 0 ? null : users.get(index);
    }
    
    private void showNickInputDialog() {
        while (true) {
            String str = JOptionPane.showInputDialog("Digite seu apelido");
            if (str == null) {
                System.exit(0);
            }
            
            if ("".equals(str.trim())) {
                continue;
            }
            
            this.nick = str;
            break;
        }
    }
    
    private void sendJoinPacket(DatagramSocket datagram) throws IOException {
        InetAddress address = InetAddress.getByName(MULTICAST_ADDRESS);
        byte[] buffer = ("JOINACK|" + nick).getBytes();
 
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, MULTICAST_PORT_SEND);
        datagram.send(packet);
    }
    
    private void startMulticastWorker() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            
            multicast = new MulticastSocket(MULTICAST_PORT_SERVER);
            multicast.joinGroup(group);
            
            new DatagramClientWorker(multicast, this)
                    .start();
            
            
            
            System.out.println("Multicast worker started.");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void startDatagramWorker() {
        try {
            
            datagram = new DatagramSocket(DATAGRAM_PORT_SERVER);
            
            new DatagramClientWorker(datagram, this)
                    .start();
            
            System.out.println("Datagram worker started.");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void startSocketWorker() {
        try {
            InetAddress address = InetAddress.getByName(TCP_ADDRESS);
            
            socket = new Socket(address, TCP_PORT);
            
            new SocketClientWorker(socket, this)
                    .start();
            
            System.out.println("TPC worker started.");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendJoinAckResponse(InetAddress address, int port) {
        try {
            String content = "JOINACK|" + nick;
            byte[] buffer = content.getBytes();
            
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            datagram.send(packet);
        } catch (IOException ex) {
            System.err.println("Failed while sending JOINACK packet");
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onPacketReceived(DatagramPacket packet) {
        return true;
    }
    
    @Override
    public void onJoinReceived(InetAddress address, int port, String nick) {
        System.out.println("onJoinReceived");
        
        if (findUserByNick(nick) == null) {
            User user = new User();
            user.address = address;
            user.port = MULTICAST_PORT_SEND;
            user.nick = nick;

            users.addElement(user);
        }
        
        sendJoinAckResponse(address, port);
        System.out.println("Sent JOINACK response packet");
    }

    @Override
    public void onJoinAckReceived(InetAddress address, int port, String nick) {
        System.out.println("onJoinAckReceived");
        System.out.println(address.toString());
        System.out.println(port);
        
        if (this.nick.equals(nick)) {
            return;
        }
        
        User user = new User();
        user.address = address;
        user.port = DATAGRAM_PORT_SERVER;
        user.nick = nick;
        
        users.addElement(user);
    }

    @Override
    public void onMessageReceived(String from, String text, boolean direct) {
        System.out.println("onMessageReceived");
        
        String message = from + " disse" + (!direct ? ": " : " (privado): ") + text + "\n";
        historyTextArea.append(message);
    }

    @Override
    public void onLeaveReceived(String nick) {
        System.out.println("onLeaveReceived");
        
        int index = findUserIndexByNick(nick);
        if (index < 0) {
            return;
        }
        
        users.remove(index);
        historyTextArea.append("** " + nick + " deixou o grupo de conversa");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        historyTextArea = new javax.swing.JTextArea();
        messageTextField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Trabalho de SD");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jScrollPane2.setViewportView(userList);

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel1.setText(" Usuarios do grupo");
        jLabel1.setName(""); // NOI18N

        historyTextArea.setEditable(false);
        historyTextArea.setColumns(20);
        historyTextArea.setRows(5);
        historyTextArea.setFocusable(false);
        jScrollPane1.setViewportView(historyTextArea);

        messageTextField.setText("Ola pessoal");

        sendButton.setText("Enviar");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(messageTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        showNickInputDialog();
        startMulticastWorker();
        startDatagramWorker();
        startSocketWorker();
        
        try {
            sendJoinPacket(datagram);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }//GEN-LAST:event_formWindowOpened

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        try {
            int index = userList.getSelectedIndex();
            User user = users.get(index);
            
            String text = messageTextField.getText()
                    .trim();
            
            if ("".equals(text)) {
                return;
            }
            
            byte[] buffer = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, user.address, user.port);
            
            datagram.send(packet);
            
            historyTextArea.append("Eu disse para " + user.nick + ": " + text + "\n");
            
        } catch (IOException ex) {
            System.err.println("Failed while sending chat message");
            ex.printStackTrace();
        }
    }//GEN-LAST:event_sendButtonActionPerformed

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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea historyTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField messageTextField;
    private javax.swing.JButton sendButton;
    private javax.swing.JList<User> userList;
    // End of variables declaration//GEN-END:variables
}
