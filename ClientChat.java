import java.net.*;
import java.util.Scanner;

public class ClientChat {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String nom = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serveur = InetAddress.getByName("localhost");
        int port = 1234;

        Thread reception = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                    socket.receive(p);
                    System.out.println(new String(p.getData(), 0, p.getLength(), "UTF-8"));
                }
            } catch (Exception e) {
                if (!socket.isClosed()) e.printStackTrace();
            }
        });
        reception.setDaemon(true);
        reception.start();

        envoyer(socket, serveur, port, "[" + nom + "] a rejoint le chat");
        System.out.println("Connecte. Tapez vos messages ('exit' pour quitter).");

        while (true) {
            String texte = sc.nextLine();
            if (texte.equalsIgnoreCase("exit")) {
                envoyer(socket, serveur, port, "[" + nom + "] a quitte le chat");
                break;
            }
            envoyer(socket, serveur, port, "[" + nom + "] : " + texte);
        }
        socket.close();
        sc.close();
    }

    private static void envoyer(DatagramSocket s, InetAddress a, int port, String msg) throws Exception {
        byte[] data = msg.getBytes("UTF-8");
        s.send(new DatagramPacket(data, data.length, a, port));
    }
}
