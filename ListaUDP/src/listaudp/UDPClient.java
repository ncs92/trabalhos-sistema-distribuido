package listaudp;

/**
 * UDPClient: Cliente UDP Descricao: Envia uma msg em um datagrama e recebe a
 * mesma msg do servidor
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class UDPClient {

    static Read receptor;
    static String nomeNick = "";

    public static void main(String args[]) {
        DatagramSocket dgramSocket;
        int resp = 0;

        try {
            dgramSocket = new DatagramSocket(5555); //cria um socket datagrama na porta 5555
            receptor = new Read(dgramSocket);
            receptor.start();

            do {
                byte[] buffer = new byte[1024];
                String ipDestino = JOptionPane.showInputDialog("Informe o IP de destino...");
                InetAddress enderecoDestino = InetAddress.getByName(ipDestino);
                String cmd = JOptionPane.showInputDialog("Informe o comando...\nADDNICK - Solicita o recebimento de mensagens,\nMSG - Envia mensagem por nick de origem.");

                if (cmd.equals("ADDNICK") || cmd.equals("MSG")) {
                    if (cmd.equals("ADDNICK")) {
                        String nick = JOptionPane.showInputDialog("Informe o nome do nick...");
                        if (nick.length() < 2 || nick.getBytes().length > 200) {
                            JOptionPane.showMessageDialog(null, "Informe um nick valido.", "Alert", JOptionPane.WARNING_MESSAGE);
                        } else {
                            //200 bytes nick, 150 bytes cmd, 674 msg
                            byte[] nickBuffer = new byte[200];
                            nickBuffer = nick.getBytes();
                            byte[] cmdBuffer = new byte[150];
                            cmdBuffer = cmd.getBytes();
                            byte[] msgBuffer = new byte[674];
                            msgBuffer = "Solicitação de contato".getBytes();

                            System.arraycopy(nickBuffer, 0, buffer, 0, 200);
                            System.arraycopy(cmdBuffer, 0, buffer, 201, 150);
                            System.arraycopy(msgBuffer, 0, buffer, 352, 674);

                            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, enderecoDestino, 5555);
                            dgramSocket.send(pacote);
                        }
                    } else if (cmd.equals("MSG")) {
                        //200 bytes nick, 150 bytes cmd, 674 msg

                        if (!nomeNick.equals("")) {
                            String msg = JOptionPane.showInputDialog("Informe a msg...");
                            int qtd = (int) Math.round((msg.getBytes().length / 674) + 0.5);
                            int qtdEnviado = 0;

                            byte[] nickBuffer = new byte[200];
                            nickBuffer = nomeNick.getBytes();
                            byte[] cmdBuffer = new byte[150];
                            cmdBuffer = cmd.getBytes();
                            byte[] msgBuffer = msg.getBytes();

                            System.arraycopy(nickBuffer, 0, buffer, 0, 200);
                            System.arraycopy(cmdBuffer, 0, buffer, 201, 150);
                            System.arraycopy(msgBuffer, 0, buffer, 352, 674);

                            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, enderecoDestino, 5555);
                            dgramSocket.send(pacote);

                        } else {
                            JOptionPane.showMessageDialog(null, "Informe o nome do nick antes de mandar uma msg.", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Informe um comando valido.", "Alert", JOptionPane.WARNING_MESSAGE);
                }

                /* armazena o IP do destino */
                resp = JOptionPane.showConfirmDialog(null, "Nova mensagem?",
                        "Continuar", JOptionPane.YES_NO_OPTION);

            } while (resp != JOptionPane.NO_OPTION);

            /* libera o socket */
            dgramSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } //catch
    } //main		      	
} //class

class Read extends Thread {

    DatagramSocket ds;
    static ArrayList<String> bloqueados = new ArrayList<>();

    public Read(DatagramSocket socket) {
        ds = socket;
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                ds.receive(pacote);
                buffer = pacote.getData();

                byte[] nickBuffer = new byte[200];
                byte[] cmdBuffer = new byte[150];
                byte[] msgBuffer = new byte[674];

                System.arraycopy(buffer, 0, nickBuffer, 0, 200);
                System.arraycopy(buffer, 201, nickBuffer, 0, 200);
                System.arraycopy(buffer, 352, nickBuffer, 0, 200);

                String nick = new String(nickBuffer);
                String cmd = new String(cmdBuffer);
                String msg = new String(msgBuffer);

                System.out.println("Recebido nick: " + nick);
                System.out.println("Recebido cmd: " + cmd);
                System.out.println("Recebido msg: " + msg);

                if (bloqueados.contains(nick)) {
                    if (cmd.equals("ADDNICK")) {
                        int escolha = JOptionPane.showConfirmDialog(null, nick + " deseja se conectar, você deseja aceitar?");
                        if (escolha == JOptionPane.YES_OPTION) {

                        } else if (escolha == JOptionPane.NO_OPTION) {
                            bloqueados.add(nick);
                        }
                    }else if (cmd.equals("MSG")){
                        System.out.println(nick + " disse: " + msg);
                    }
                }else{
                    System.out.println(nick + " tentou se conectar, mas está bloqueado");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
