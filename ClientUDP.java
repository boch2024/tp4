import java.net.*;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String nom = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serveur = InetAddress.getByName("localhost");
        System.out.println("Tapez vos messages ('exit' pour quitter).");

        while (true) {
            String texte = sc.nextLine();
            if (texte.equalsIgnoreCase("exit")) break;
            byte[] data = ("[" + nom + "] : " + texte).getBytes("UTF-8");
            socket.send(new DatagramPacket(data, data.length, serveur, 1234));
        }
        socket.close();
        sc.close();
    }
}
