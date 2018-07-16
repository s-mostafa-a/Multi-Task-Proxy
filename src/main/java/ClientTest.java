import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by ahmadi on 7/14/18.
 */
public class ClientTest {
    /**
     * keep in mind, client entry should be single line, but response from server can be multi-line
     * */
    public static void main(String[] args) {
        // sample inputs:
        //                udp 127.0.0.1 8001
        //                tcp 127.0.0.1 8001
        // then
        //                GET / HTTP/1.1 Host: http://www.aut.ac.ir
        //                GET / HTTP/1.1 Host: https://httpstat.us/301
        //                type=A server=217.215.155.155 target=aut.ac.ir
        //                type=CNAME server=8.8.8.8 target=www.bbc.com
        Client client = new Client();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        StringTokenizer st;
        System.out.println("Enter your client type, proxy server's ip, and port:");
        try {
            st = new StringTokenizer(br.readLine());
            String s = st.nextToken();
            if (s.compareTo("udp") == 0) {
                client = new udpClient();
            } else if (s.compareTo("tcp") == 0) {
                client = new tcpClient();
            }
            String serverIP = st.nextToken();
            int port = Integer.parseInt(st.nextToken());
            // sending the request is all on client's class side
            System.out.println("Enter your request: ");
            String message = br.readLine();
            client.setRequestMessage(message);
            client.setServerIP(serverIP);
            client.setServerPort(port);
            //
            client.run();
            //client send


        } catch (Exception e) {

        }
    }

}
