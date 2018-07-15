import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by ahmadi on 7/14/18.
 * receives udp and sends tcp. in return receives tcp and sends udp to first sender.
 */
public class ProxyTest {

    public static void main(String[] args) {
        //sample input : -d udp -s tcp:127.0.0.1:8001
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String sourceProtocol = "";
        String destProtocol = "";
        String sourceIp = "";
        String sourcePort = "";
        StringTokenizer st;
        int state = 0;
        try {
            st = new StringTokenizer(br.readLine());
            while (state != 2) {
                String s1 = st.nextToken();
                if (s1.compareTo("-d") == 0) {
                    destProtocol = st.nextToken();
                    state++;
                } else if (s1.compareTo("-s") == 0) {
                    String s = st.nextToken();
                    char[] chars = s.toCharArray();
                    sourceProtocol = "" + chars[0] + chars[1] + chars[2];
                    int i = 4;
                    for (; i < s.length(); i++) {
                        if (chars[i] == ':')
                            break;
                        sourceIp = sourceIp + chars[i];
                    }
                    i++;
                    for (; i < s.length(); i++) {
                        if (chars[i] == ' ')
                            break;
                        sourcePort = sourcePort + chars[i];
                    }
                    state++;

                } else
                    System.err.println("Input error");
            }
            System.out.println(sourceProtocol);
            System.out.println(sourceIp);
            System.out.println(sourcePort);
            System.out.println(destProtocol);
            ProxyServer proxyServer;
            if (sourceProtocol.compareTo("udp") == 0)
                new udpClientsProxyServer(sourceIp, Integer.parseInt(sourcePort)).run();
            else
                new tcpClientsProxyServer(sourceIp, Integer.parseInt(sourcePort)).run();
        } catch (IOException e) {
        }
    }
}
