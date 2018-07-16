import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by ahmadi on 7/16/18.
 */
public class udpClientsProxyServerThread extends Thread {
    private InetAddress ip;
    private int port;
    private String lastAck;
    private int lastIndexSent;
    private String URL;
    private ArrayList<String> messages;

    public udpClientsProxyServerThread(InetAddress ip, int port) {
        super();
        System.out.println("New thread starting!");
        this.port = port;
        this.ip = ip;
        lastIndexSent = 0;
        messages = new ArrayList<String>();
    }

    public void ackReceived(String ack) throws IOException {
        lastAck = ack;
        sendNext();
    }

    public void sendNext() throws IOException {
        if (Integer.parseInt(lastAck) == (2 + lastIndexSent) % 2)
            return;
        DatagramSocket clientSocket = new DatagramSocket();
        lastIndexSent++;
        String toBeSent = messages.get(lastIndexSent);
        byte[] sendData;
        sendData = toBeSent.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setRequest(String request) {
        request = request.toLowerCase();
        char[] rchars = request.toCharArray();
        int index = request.indexOf("host:") + 6;
        //working with string
        URL = "";
        for (int i = index; i < request.length(); i++) {
            if (rchars[i] == ' ')
                break;
            URL = URL + rchars[i];
        }
        System.out.println("URL: " + URL);

    }

    @Override
    public void run() {
        System.out.println("Running thread");
        try {
            // get request to web-server
            String response = resolve(URL);
            // segmenting data
            for (int i = 0; i <= (int) (response.length()) / 1000; i++) {
                String edame = "1";
                String seqNum = "0";
                int finish = (i + 1) * 1000 - 1;
                if (i == (int) (response.length()) / 1000) {
                    edame = "0";
                    finish = response.length() - 1;
                }
                if (i % 2 == 1) seqNum = "1";
                messages.add(edame + seqNum + response.substring(i * 1000, finish));
            }
            //sending first part
            lastIndexSent = -1;
            sendNext();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Thread finishing!");
    }

    //works fine
    //sends get request and returns answer
    private String resolve(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            return "";
        }
    }

}