import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by ahmadi on 7/14/18.
 */
public class udpClientsProxyServer extends ProxyServer {
    ArrayList<udpClientsProxyServerThread> workers;
    public udpClientsProxyServer(String sourceIP, int sourcePort) {
        super(sourceIP, sourcePort);
        workers = new ArrayList<udpClientsProxyServerThread>();
    }

    @Override
    public void run() {
        DatagramSocket server;
        try {
            server = new DatagramSocket(sourcePort);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        System.out.println("Server listening on port " + sourcePort + " for udp!");
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            server.receive(receivePacket);
            //inja stuck mishe
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            try {
            } catch (IOException e) {
                e.printStackTrace();
            }
            new tcpClientsProxyServerThread(socket).start();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
