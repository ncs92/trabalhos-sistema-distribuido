package exercicio5;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import static com.oracle.jrockit.jfr.ContentType.Bytes;
import com.sun.istack.internal.logging.Logger;
import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class TCPServer {

    public static List<Usuario> usuarios = new ArrayList<Usuario>();

    public static void main(String args[]) {
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();
                usuarios.add(new Usuario("", clientSocket));

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                LerMensagem ler;
                ler = new LerMensagem(clientSocket, new TCPClient.OnMensagemRecebida() {
                    @Override
                    public void onMensagemRecebida(byte[] mensagem) {
                        System.out.println("S - Comando: " + mensagem[1]);
                        System.out.println("S - Tamanho: " + mensagem.length);

                        Byte tam = mensagem[2];
                        int tamArq = tam.intValue();

                        byte[] nome = Arrays.copyOfRange(mensagem, 3, mensagem[2] + 3);
                        String nomeArquivo = new String(nome);
                        System.out.println("Arquivo solicitado: " + nomeArquivo);
                        byte[] retorno = new byte[258];
                        retorno[0] = 2;
                        retorno[1] = mensagem[1];
                        retorno[2] = 1;

                        if (mensagem[1] == 2) { //DELETE 
                            File arq = new File("C:\\Users\\Elaine\\Documents\\shared\\" + nomeArquivo);

                            boolean deletado = arq.delete();

                            if (!deletado) {
                                retorno[2] = 2;
                            }
                        } else if (mensagem[1] == 3) { // GETFILESLIST 
                            File file = new File("C:\\Users\\Elaine\\Documents\\shared");
                            File todosArquivos[] = file.listFiles();
                            String listaNomes = "";
                            int pos = 5;

                            for (int i = 0; i < todosArquivos.length; i++) {
                                File arquivos = todosArquivos[i];
                                listaNomes += arquivos.getName() + "\n";
                                retorno[pos++] = (byte) arquivos.getName().length();

                                int tamanho = arquivos.getName().getBytes().length;
                                byte[] nomeArq = new byte[tamanho];
                                nomeArq = arquivos.getName().getBytes();
                                for (int j = 0; j < tamanho; j++) {
                                    retorno[pos++] = nomeArq[j];
                                }
                            }

                            BigInteger bigInt = BigInteger.valueOf(todosArquivos.length);
                            byte[] intToByte = bigInt.toByteArray();
                            System.out.println("Total Arq :" + todosArquivos.length);
                            System.out.println("Total Arq :" + bigInt.toByteArray().length);

                            if (bigInt.toByteArray().length > 1) {
                                retorno[3] = bigInt.toByteArray()[0];
                                retorno[4] = bigInt.toByteArray()[1];
                            } else {
                                retorno[3] = 0;
                                retorno[4] = bigInt.toByteArray()[0];
                            }

                        } else if (mensagem[1] == 4) { // GETFILE  
                            File arq = new File("C:\\Users\\Elaine\\Documents\\shared\\" + nomeArquivo);

                            BigInteger bigInt = BigInteger.valueOf(nomeArquivo.length());
                            byte[] intToByte = bigInt.toByteArray();
                            byte[] cabecalho = new byte[7];
                            cabecalho[0] = 2;
                            cabecalho[1] = mensagem[1];
                            cabecalho[2] = 1;

                            if (bigInt.toByteArray().length > 1) {
                                switch (bigInt.toByteArray().length) {
                                    case 2:
                                        retorno[3] = 0;
                                        retorno[4] = 0;
                                        retorno[5] = bigInt.toByteArray()[0];
                                        retorno[6] = bigInt.toByteArray()[1];
                                        break; // optional
                                    case 3:
                                        retorno[3] = 0;
                                        retorno[4] = bigInt.toByteArray()[0];
                                        retorno[5] = bigInt.toByteArray()[1];
                                        retorno[6] = bigInt.toByteArray()[2];
                                        break; // optional                                        
                                    case 4:
                                        retorno[3] = bigInt.toByteArray()[0];
                                        retorno[4] = bigInt.toByteArray()[1];
                                        retorno[5] = bigInt.toByteArray()[2];
                                        retorno[6] = bigInt.toByteArray()[3];
                                        break; // optional
                                    default:
                                        break;
                                }
                            } else {
                                retorno[3] = 0;
                                retorno[4] = 0;
                                retorno[5] = 0;
                                retorno[6] = bigInt.toByteArray()[0];
                            }

                            FileInputStream in = null;
                            try {
                                in = new FileInputStream(arq);
                            } catch (FileNotFoundException ex) {
                                java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            byte[] bytesLidos = new byte[1024];
                            int qtdLida;

                            try {
                                while ((qtdLida = in.read(bytesLidos)) != -1) {
                                    for (Usuario usuario : usuarios) {
                                        try {
                                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                            outputStream.write(cabecalho);
                                            outputStream.write(bytesLidos);

                                            new DataOutputStream(usuario.socket.getOutputStream())
                                                    .write(outputStream.toByteArray(), 0, qtdLida);
                                        } catch (IOException ex) {
                                            java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                }
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        for (Usuario usuario : usuarios) {
                            try {
                                new DataOutputStream(usuario.socket.getOutputStream())
                                        .write(retorno);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });

                ler.start();

            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class
