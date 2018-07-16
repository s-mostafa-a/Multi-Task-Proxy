import java.io.IOException;

/**
 * Created by ahmadi on 7/14/18.
 * sends udp to proxy server
 */
public class Client {
    protected String requestMessage;
    protected String serverIP;
    protected int serverPort;

    Client() {
    }

    public void setRequestMessage(String message) {
        requestMessage = message;
    }

    public void setServerIP(String ip) {
        this.serverIP = ip;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void run() throws IOException {
    }
}
