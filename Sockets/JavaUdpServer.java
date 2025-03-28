import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class JavaUdpServer {

    public static void main(String args[])
    {
        System.out.println("JAVA UDP SERVER");
        DatagramSocket socket = null;
        int portNumber = 9009;

        try{
            socket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[1024];

            while(true) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

//                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
//                System.out.println("received msg: " + msg);
//
//                byte[] sendBuffer = "Response from server".getBytes();
//                socket.send(new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort()));

                ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
                wrapped.order(ByteOrder.LITTLE_ENDIAN);
                int receivedNumber = wrapped.getInt();
                System.out.println("Received number: " + receivedNumber);

                receivedNumber += 1;
                byte[] sendBuffer = new String("Response from server " + receivedNumber).getBytes();
                socket.send(new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort()));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
