import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client implements Runnable {
    private final Socket socket;
    private final DatagramSocket datagramSocket;
    private final MulticastSocket multicastSocket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final InetAddress serverAddress;
    private final int serverPort;
    private final InetAddress groupAddress;
    private int clientId;

    public Client(String hostName, int portNumber) throws IOException {
        this.socket = new Socket(hostName, portNumber);
        this.datagramSocket = new DatagramSocket(socket.getLocalPort());

        this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.serverAddress = InetAddress.getByName(hostName);
        this.serverPort = portNumber;
        this.groupAddress = InetAddress.getByName("230.0.0.0");
        this.multicastSocket = new MulticastSocket(9999);
        this.multicastSocket.joinGroup(new InetSocketAddress(groupAddress, 9999), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 12345;

        try {
            Client client = new Client(hostName, portNumber);
            client.run();

        } catch (IOException ex) {
            System.err.println("Client failed to start: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        String message;

        new Thread(this::listenForTCPMessage).start();
        new Thread(this::listenForUDPMessage).start();
        new Thread(this::listenForUDPMulticast).start();

        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String userMessage;

            while (socket.isConnected() && !datagramSocket.isClosed()) {
                userMessage = userInput.readLine();

                if (userMessage.equals("U")) {
                    message = "Client " + clientId + ":" + getAsciiArt();
                    datagramSocket.send(new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort));
                } else if (userMessage.equals("M")) {
                    message = "Client " + clientId + ":" + getAsciiArt2();
                    datagramSocket.send(new DatagramPacket(message.getBytes(), message.length(), groupAddress, 9999));
                } else if (!userMessage.isEmpty())
                    writer.println(userMessage);
            }
        } catch (IOException e) {
            closeTCPConnection();
            closeUDPConnection();
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    public void listenForUDPMessage() {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        while (!datagramSocket.isClosed()) {
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                datagramSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(message);

            } catch (IOException e) {
                closeUDPConnection();
            }
        }
    }

    public void listenForUDPMulticast() {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        while (!multicastSocket.isClosed()) {
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                multicastSocket.receive(receivePacket);
                int senderPort = receivePacket.getPort();

                if (senderPort != datagramSocket.getLocalPort()) {
                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(message);
                }

            } catch (IOException e) {
                closeUDPConnection();
            }
        }
    }

    public void listenForTCPMessage() {
        String serverMessage;
        try {
            serverMessage = reader.readLine();
            this.clientId = Integer.parseInt(serverMessage);
            System.out.println(("Received Id: " + clientId));

            while (socket.isConnected()) {
                serverMessage = reader.readLine();
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            closeTCPConnection();
            System.err.println("Disconnected from server.");
        }
    }

    public String getAsciiArt() {
        return "\n  __________\n" +
                " / ___  ___ \\\n" +
                "/ / @ \\/ @ \\ \\\n" +
                "\\ \\___/\\___/ /\\\n" +
                " \\____\\/____/||\n" +
                " /     /\\\\\\\\\\//\n" +
                "|     |\\\\\\\\\\\\\n" +
                " \\      \\\\\\\\\\\\\n" +
                "   \\______/\\\\\\\\\n" +
                "    _||_||_";
    }

    public String getAsciiArt2() {
        return "\n                 ***          ***\n" +
                "              ***....**     **...***\n" +
                "             **........** **.......**\n" +
                "      ***    **..........*.........**    ***\n" +
                "   **.....**  **..................**  **.....**\n" +
                " **.........**  **..............**  **.........**\n" +
                "*..............*   *..........*   *..............*\n" +
                " **..............*   *......*   *..............**\n" +
                "   **..............** *....* **..............**\n" +
                "     *......................................*\n" +
                "   **..............**........**..............**\n" +
                " **..............*    *....*    *..............**\n" +
                "*..............*      *....*      *..............*\n" +
                " **.........**        *....*        **.........**\n" +
                "   **.....**         *....*           **.....**\n" +
                "      ***          **....*               ***\n" +
                "                 ** * * *";
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

    public void closeUDPConnection() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
    }
}
