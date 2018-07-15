import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by ahmadi on 7/14/18.
 */
public class ClientTest {
    public static void main(String[] args) {
        // sample input: GET / HTTP/1.1 Host: aut.ac.ir
        // type=A server=217.215.155.155 target=aut.ac.ir
        Client client = new Client();
        boolean requestSuccseed = false;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        StringTokenizer st;
        System.out.println("Enter your client type:");
        try {
            st = new StringTokenizer(br.readLine());
            String s = st.nextToken();
            if (s.compareTo("udp") == 0) {
                client = new udpClient();
            } else if (s.compareTo("tcp") == 0) {
                client = new tcpClient();
            }
            while (!requestSuccseed) {
                System.out.println("Enter your request: ");
                String message = br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    message = message + line;
                }
                client.setRequestMessage(message);
                message.toLowerCase();
                String hostString = getHostAddress(message);
                client.setHost(hostString);
                //
                client.run();
                //client send
                int counter = 0;
                while (!client.isRequestAnswered()) {
                    Thread.sleep(100);
                    counter++;
                    if (counter == 100) {
                        System.out.println("Response not received!");
                        break;
                    }
                }
                if (counter < 100) {
                    requestSuccseed = true;
                }
                if (client.responseNumber != 200) {
                    System.out.println("Your request failed with code: " + client.responseNumber);
                    requestSuccseed = false;
                }
            }
        } catch (Exception e) {

        }
    }

    public static String getHostAddress(String string) {
        int index = string.indexOf("host:") + 5;
        char[] chars = string.toCharArray();
        String host = "";
        if (chars[index] == ' ') {
            index++;
        }
        for (int i = index; i < string.length(); i++) {
            if (chars[i] == ' ')
                break;
            host = host + chars[i];
        }
        return host;
    }
}
