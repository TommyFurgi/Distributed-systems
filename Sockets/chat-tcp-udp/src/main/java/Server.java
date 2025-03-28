import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final DatagramSocket datagramSocket;
    private final ExecutorService pool;
    private final int maxClients;
    private Map<Integer, ClientHandler> clients = Collections.synchronizedMap(new HashMap<>());

    private int nextClientId = 1;

    public Server(int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(poolSize);
        this.maxClients = poolSize;
        this.datagramSocket = new DatagramSocket(port);
    }

    public static void main(String[] args) {
        int port = 12345;
        int poolSize = 10;
        try {
            Server tcpServer = new Server(port, poolSize);
            tcpServer.run();

        } catch (IOException ex) {
            System.err.println("Server failed to start: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            System.out.println("Server is running and waiting for clients!");

            new Thread(this::handleUdpMessage).start();

            while (!serverSocket.isClosed()) {
                if (clients.size() < maxClients) {
                    Socket tcpSocket = serverSocket.accept();

                    ClientHandler handler = new ClientHandler(tcpSocket, this, nextClientId);
                    clients.put(nextClientId, handler);
                    System.out.println("New client with ID: " + nextClientId + " connected!");
                    nextClientId += 1;
                    pool.execute(handler);
                } else {
                    System.out.println("Server full, rejecting new connection.");
                    Thread.sleep(1000);
                }
            }
        } catch (IOException | InterruptedException ex) {
            pool.shutdown();
            closeTCPConnection();
        }
    }

    private void handleUdpMessage() {
        byte[] receiveBuffer = new byte[1024];

        try {
            while (!datagramSocket.isClosed()) {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                this.datagramSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Received UDP message: " + message);

                for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
                    if (receivePacket.getPort() != entry.getValue().getClientPort()) {
                        datagramSocket.send(new DatagramPacket(message.getBytes(), message.length(), entry.getValue().getClientAddress(), entry.getValue().getClientPort()));
                    }
                }
            }
        } catch (IOException ex) {
            closeUDPConnection();
        }
    }

    public void broadcast(int senderId, String message) {
        System.out.println("Client with Id " + senderId + " sent message: " + message);
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if (entry.getKey() != senderId) {
                entry.getValue().sendMessage("Client " + senderId + ": " + message);
            }
        }
    }

    public void removeClient(int clientId) {
        clients.remove(clientId);
    }

    public void closeTCPConnection() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeUDPConnection() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
    }
}
