import socket;

serverPort = 9009
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
serverSocket.bind(('', serverPort))
buff = []

print('PYTHON UDP SERVER')

while True:

    buff, address = serverSocket.recvfrom(1024)
    print("python udp server received msg: " + str(buff, 'cp1250'))
    decoded_msg = buff.decode('cp1250')

    if "Python" in decoded_msg:
        response = "Pong Python"
    elif "Java" in decoded_msg:
        response = "Pong Java"
    else:
        response = "Pong Unknown"


    serverSocket.sendto(response.encode('cp1250'), address)

