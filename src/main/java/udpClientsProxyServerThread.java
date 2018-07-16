import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by ahmadi on 7/16/18.
 */
public class udpClientsProxyServerThread extends Thread {
    private InetAddress ip;
    private int port;
    private String lastAck;
    private int lastIndexSent;
    private String URL;
    private String request;
    public udpClientsProxyServerThread( InetAddress ip, int port) {
        super();
        System.out.println("New thread starting!");
        this.port = port;
        this.ip = ip;
        lastIndexSent = 0;
    }

    public void ackReceived(String ack){
        lastAck = ack;
        sendNext();
    }
    public void sendNext(){

    }
    public void setURL(String URL){
        this.URL = URL;
    }
    public void setRequest(String request){
        this.request = request;
    }
    @Override
    public void run() {
        System.out.println("Running thread");
        String clientEntry = request;
        try {
            // address ro darbiar, resolve ro seda bezan, segmant kon, sendNext ro seda kon
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Thread finishing!");
    }

    //works fine
    private String resolve(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // success
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
