package exercicio1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class Client implements Reader.Listener {
    
    private static final int PORT = 5555;
    
    private static final String[] VALID_COMMANDS = new String[]{
        "ADDNICK",
        "MSG",
        "ACC",
        "BLK",
        "EXIT"
    };
    
    private final String nick;
    private final DatagramSocket socket;
    
    private final List<String> pendentes;
    private final List<String> bloqueados;
    private final List<String> permitidos;
    
    private Client(String nick, DatagramSocket socket) {
        this.nick = nick;
        this.socket = socket;
        
        pendentes = new ArrayList<>();
        bloqueados = new ArrayList<>();
        permitidos = new ArrayList<>();
    }
    
    private String showCommandoInputDialog() {
        List<String> list = Arrays.asList(VALID_COMMANDS);
        
        StringBuilder message = new StringBuilder();
        message.append("Digite um comando:\n\n");
        message.append("ADDNICK - Solicita adiçao do seu NICK na lista de recebimento de mensagens\n");
        message.append("MSG - Envia mensagem para um IP ou NICK\n");
        message.append("ACC - Aceitar NICK pendente\n");
        message.append("BLK - Bloquear NICK pendente\n");
        message.append("EXIT - Sair do programa\n");
        
        String command = "";
        do {
            command = JOptionPane.showInputDialog(message);
        } while (!list.contains(command) || !command.equals("EXIT"));

        return command;
    }
    
    private String showManageNickInputDialog(boolean block) {
        StringBuilder message = new StringBuilder();
        if (block) {
            message.append("Bloquear NICK pendente:\n");
        } else {
            message.append("Aceitar NICK pendente:\n");
        }
        
        message.append("Digite o numero correspondente:\n\n");
        
        int count = 1;
        for (final String pendente : pendentes) {
            message.append(count);
            message.append(" - ");
            message.append(pendente);
            message.append("\n");
            
            count++;
        }
        
        message.append("0 - Voltar ao menu anterior\n");
        
        return JOptionPane.showInputDialog(message);
    }

    @Override
    public void onAddNickReceived(String nick) {
        pendentes.add(nick);
    }

    @Override
    public void onMessageReceived(String nick, String msg) {
        if (bloqueados.contains(nick)) {
            System.out.println(nick + " esta tentando se conectar mas esta bloqueado");
            return;
        }
        
        if (!permitidos.contains(nick)) {
            System.out.println(nick + " esta tentando enviar mensagem mas nao esta permitido");
            return;
        }
        
        System.out.println(nick + " disse: " + msg);
    }
    
    public void start() throws IOException {
        new Thread(new Reader(socket, this))
                .start();
        
        while (true) {
            byte[] buffer = new byte[1024];
            
            String command = showCommandoInputDialog();
            if ("EXIT".equals(command)) {
                System.exit(0);
                
            } else if ("ACC".equals(command)) {
                String acc = showManageNickInputDialog(false);
                int index = Integer.parseInt(acc) - 1;
                if (index >= 0) {
                    String pendente = pendentes.get(index);
                    permitidos.add(pendente);
                    pendentes.remove(index);
                    
                    int i = bloqueados.indexOf(pendente);
                    if (i >= 0) {
                        bloqueados.remove(index);
                    }
                }
                
                continue;
                
            } else if ("BLK".equals(command)) {
                String blk = showManageNickInputDialog(true);
                int index = Integer.parseInt(blk) - 1;
                if (index >= 0) {
                    String pendente = pendentes.get(index);
                    
                    bloqueados.add(pendente);
                    pendentes.remove(index);
                    
                    int i = permitidos.indexOf(pendente);
                    if (i >= 0) {
                        permitidos.remove(index);
                    }
                }
                
                continue;
            }
        
            String destino = JOptionPane.showInputDialog("Informe o IP ou NICK de destino");
            InetAddress ip = InetAddress.getByName(destino);
            
            System.arraycopy(nick.getBytes(), 0, buffer, 0, nick.length());
            System.arraycopy(command.getBytes(), 0, buffer, 201, command.length());
            
            if ("MSG".equals(command)) {
                String message = JOptionPane.showInputDialog("Informe a mensagem para enviar");
                System.arraycopy(message.getBytes(), 0, buffer, 402, message.length());
                
            } else if ("ADDNICK".equals(command)) {
                System.out.println("Solicitado adiçao do NICK: " + nick);
            }

            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, ip, PORT);
            socket.send(pacote);
        }
    }
    
    public DatagramSocket getSocket() {
        return socket;
    }
    
    public boolean isBlocked(String nick) {
        return true;
    }
    
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        
        String nick = JOptionPane.showInputDialog("Informe o seu NICK");
        DatagramSocket socket = new DatagramSocket(PORT);
        
        Client client = new Client(nick, socket);
        client.start();
    }
}
