import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ahmadi on 7/14/18.
 */
public class udpClientsProxyServer extends ProxyServer {
    ArrayList<udpClientsProxyServerThread> workers;
    ArrayList<String> requestedURLs;
    ArrayList<String> answers;
    public udpClientsProxyServer(String sourceIP, int sourcePort) {
        super(sourceIP, sourcePort);
        workers = new ArrayList<udpClientsProxyServerThread>();
        requestedURLs = new ArrayList<String>();
        answers = new ArrayList<String>();
    }

    @Override
    public void run() {
        DatagramSocket server;
        try {
            server = new DatagramSocket(sourcePort);
            System.out.println("Server listening on port " + sourcePort + " for udp!");
            while (true) {
                byte[] receiveData = new byte[1024];
                boolean flag = false;
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                server.receive(receivePacket);
                //inja stuck mishe
                String sentence = new String(receiveData,0,receiveData.length);
                System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                for (int i = 0; i < workers.size(); i++) {
                    if ((workers.get(i).getPort() == port)&&(Arrays.equals(workers.get(i).getIp().getAddress(), IPAddress.getAddress()))){
                        if (workers.get(i).ackReceived(sentence))
                            workers.remove(i);
                        flag = true;
                        System.out.println("This was Ack!");
                    }
                }
                if (!flag) {
                    System.out.println("This was request!");
                    udpClientsProxyServerThread jadid = new udpClientsProxyServerThread(IPAddress, port);
                    requestedURLs.add(jadid.setRequest(sentence));
                    workers.add(jadid);
                    jadid.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
