import socket
from Gerenciamento_pb2 import Gerenciamento 
import base64

g = Gerenciamento()
print("Executando...")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(('127.0.0.1', 5555))
s.listen(10)
con, cliente = s.accept()
print ('Conectado por', cliente)
msg = con.recv(1024)
aux = msg.decode('utf-8')
print(aux)
#print('mensagem enviada: ', g.ParseFromString(base64.b64decode(msg).decode('utf-8')))
print ('Finalizando conexao do cliente', cliente)
    

s.close()