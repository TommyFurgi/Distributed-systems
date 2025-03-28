import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server tcpServer;
    private final int clientId;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final int clientPort;
    private final InetAddress clientAddress;

    ClientHandler(Socket socket, Server tcpServer, int clientId) throws IOException {
        this.socket = socket;
        this.tcpServer = tcpServer;
        this.clientId = clientId;
        this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.clientPort = socket.getPort();
        this.clientAddress = socket.getInetAddress();
    }

    public void run() {
        try {
            String message;
            sendMessage(Integer.toString(clientId));


            while (socket.isConnected()) {
                message = reader.readLine();
                tcpServer.broadcast(clientId, message);

            }
        } catch (IOException e) {
            System.out.println("Client with Id: " + clientId + " disconnected!");
        } finally {
            tcpServer.removeClient(this.clientId);
            closeTCPConnection();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void closeTCPConnection() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientPort() {
        return clientPort;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

}
