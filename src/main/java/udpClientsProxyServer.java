import java.io.IOException;
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
            System.out.println("Server listening on port " + sourcePort + " for udp!");
            while (true) {
                boolean flag = false;
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                server.receive(receivePacket);
                //inja stuck mishe
                String sentence = new String(receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                for (int i = 0; i < workers.size(); i++) {
                    if ((workers.get(i).getPort() == port)&&(workers.get(i).getIp().getAddress() == IPAddress.getAddress())){
                        workers.get(i).ackReceived(sentence);
                        flag = true;
                        System.out.println("This was Ack!");
                    }
                }
                if (!flag) {
                    System.out.println("This was request!");
                    udpClientsProxyServerThread jadid = new udpClientsProxyServerThread(IPAddress, port);
                    jadid.setRequest(sentence);
                    workers.add(jadid);
                    jadid.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
