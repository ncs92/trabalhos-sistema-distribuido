# Trabalhos da Disciplina de Sistemas Distribuídos
## Bacharelado em Ciência da Computação - UTFPR - Campus Campo Mourão


Lista de exercícios:

1) Fazer o chat P2P  usando UDP com a porta 5555.
O chat deve possuir as seguintes funcionalidades:
- aceitar ou bloquear usuários via nick (apelido)
- enviar e receber mensagens via nick e via IP
O formato das mensagens deve seguir o seguinte padrão:
NICKorigem, CMD, DADOS
Se CMD:
- ADDNICK: solicita o recebimento de mensagens com origem de NICKorigem. DADOS = "Mensagem qualquer de solicitação".
- MSG: mensagem enviada por NICKorigem. DADOS = "conteúdo da mensagem". Se ADDNICK não foi enviado ou está na lista de bloqueio, recuse a mensagem.


2) Fazer um sistema de upload de arquivos via UDP. Um servidor UDP deverá receber as partes dos arquivos (1024 bytes), verificar ao final a integridade via um checksum (MD5) e armazenar o arquivo em uma pasta padrão.






Trabalho

Implementar um serviço de chat que possibilite:
- envio de mensagens para um grupo de pessoas (MulticastSocket)
- envio de mensagens individuais para as pessoas ativas (DatagramSocket) - receber na porta 6799
- compartilhamento e download de arquivos (Socket -- TCP)
- interface de interação (GUI ou CLI)

- protocolo textual:
   -- JOIN [apelido]  (via MULTICAST)
   * junta-se ao grupo de conversação
   * ex: JOIN [Campiolo]

   -- JOINACK [apelido-de-quem-recebeu-join]
   * resposta ao JOIN para possibilitar a manutenção da lista de usuários ativos (via UDP)
   * ex: JOINACK [Dracula]

   -- MSG [apelido] "texto"
   * mensagem enviada a todos os membros do grupo pelo IP 225.1.2.3 e porta 6789 (via MULTICAST)
   * ex: MSG [Campiolo] "Bom dia pessoal."

   -- MSGIDV FROM [apelido] TO [apelido] "texto" (via UDP)
   * mensagem enviada a um membro do grupo para ser recebida na porta 6799
   * ex: MSGIDV FROM [Campiolo] TO [Bicho Papão] "Hoje é sexta-feira 13."

   -- LISTFILES [apelido]
   * solicitação de listagem de arquivos para um usuário (via UDP)
   * ex: LISTFILES [Bicho Papão]

   -- FILES [arq1, arq2, arqN]
   * resposta para o LISTFILES (via UDP)
   * ex: FILES [lista_alunos_visitar.txt, lista_alunos_visitados.txt, arquivo_surpresa.exe]

   -- DOWNFILE [apelido] filename
   * solicita arquivo do servidor.  (via UDP)
   * ex: DOWNFILE [Bicho Papão] lista_alunos_visitar.txt

   -- DOWNINFO [filename, size, IP, PORTA]
   * resposta com informações sobre o arquivo e conexão TCP.
   * ex: DOWNINFO [lista_alunos_visitar.txt, 50000, 192.168.10.2, 7777]

   -- LEAVE [apelido] * deixa o grupo de conversação
   * mensagem enviada ao grupo informando que deixou a conversação.
   * ex: LEAVE [Campiolo]
