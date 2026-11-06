import java.net.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServeurChat {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(1234));
        System.out.println("Serveur de chat UDP en ecoute sur le port 1234...");

        Set<SocketAddress> clients = new LinkedHashSet<>();
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            SocketAddress expediteur = packet.getSocketAddress();
            String msg = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

            if (clients.add(expediteur)) {
                System.out.println("Nouveau client : " + expediteur);
            }
            System.out.println("Recu de " + expediteur + " -> " + msg);

            byte[] data = msg.getBytes("UTF-8");
            for (SocketAddress client : clients) {
                if (!client.equals(expediteur)) {
                    socket.send(new DatagramPacket(data, data.length, client));
                }
            }
        }
    }
}
