import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ServeurUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(1234));
        System.out.println("Serveur UDP en ecoute sur le port 1234...");

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            System.out.println("De " + packet.getAddress().getHostAddress()
                    + ":" + packet.getPort() + " -> " + msg);
        }
    }
}
