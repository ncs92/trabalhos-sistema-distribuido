Compilação: 
    - Execute o comando: java *.java na pasta

Execução:
    - Inicializar o servidor de nomes
        rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false
    - Inicializar o servidor
        java -Djava.security.policy=policy.txt -Djava.rmi.server.codebase=file:. Servidor
    - Inicializar o cliente
        java -Djava.security.policy=policy.txt Cliente

bibliotecas padroes do rmi, utils e lang

Exemplo:
    Adiciona um livro:
        - livros = c.adicionar(livros, "Garota Exemplar - Gillian Flynn");
    Lista os livros:
        - c.listar(livros);
    Remove um livro pelo nome:
        - livros = c.excluir(livros, "Lexico - Max Barry");
