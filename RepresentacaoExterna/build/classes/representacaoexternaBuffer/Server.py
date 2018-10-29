import socket
from Gerenciamento_pb2.py import Gerenciamento

g = Gerenciamento_pb2.Gerenciamento()
print("Executando...")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(('127.0.0.1', 5555))
s.listen(10)
con, cliente = s.accept()
print ('Conectado por', cliente)
msg = con.recv(1024)
print('mensagem enviada: ', msg)
print ('Finalizando conexao do cliente', cliente)
    

s.close()