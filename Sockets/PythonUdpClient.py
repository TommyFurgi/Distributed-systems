import socket;

serverIP = "127.0.0.1"
serverPort = 9009
msg = "żółta gęś"

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# client.sendto(bytes(msg, 'utf-8'), (serverIP, serverPort))

# msg_bytes = (300).to_bytes(4, byteorder='little')
# client.sendto(msg_bytes, (serverIP, serverPort))
# buff, address = client.recvfrom(1024)
# print("received msg: " + str(buff, 'cp1250'))

client.sendto(bytes("Ping Python UDP", 'utf-8'), (serverIP, serverPort))
buff = []
buff, address = client.recvfrom(1024)
print("python udp client received msg: " + str(buff, 'cp1250'))



