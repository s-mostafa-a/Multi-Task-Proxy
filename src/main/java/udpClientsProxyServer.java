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
                String sentence = new String(receiveData, 0, receiveData.length);
                System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                for (int i = 0; i < workers.size(); i++) {
                    if ((workers.get(i).getPort() == port) && (Arrays.equals(workers.get(i).getIp().getAddress(), IPAddress.getAddress()))) {
                        workers.get(i).ackReceived(sentence);
                        flag = true;
                        System.out.println("This was Ack!");
                    }
                }
                if (!flag) {
                    System.out.println("This was request!");

                    // working with string
                    String newSentence = sentence.toLowerCase();
                    char[] rchars = newSentence.toCharArray();
                    int index = newSentence.indexOf("host:") + 6;
                    //working with string
                    String URL = "";
                    for (int i = index; i < newSentence.length(); i++) {
                        if (rchars[i] == ' ' || rchars[i] == '\u0000')
                            break;
                        URL = URL + rchars[i];
                    }
                    //
                    if (!requestedURLs.contains(URL)) {
                        udpClientsProxyServerThread jadid = new udpClientsProxyServerThread(IPAddress, port);
                        requestedURLs.add(jadid.setRequest(sentence));
                        workers.add(jadid);
                        jadid.start();
                    }
                    else{
                        for (int i = 0; i < workers.size(); i++){
                            if (URL.compareTo(workers.get(i).getURL()) == 0){
                                workers.get(i).reset(IPAddress, port);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
