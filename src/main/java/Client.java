import java.io.IOException;

/**
 * Created by ahmadi on 7/14/18.
 * sends udp to proxy server
 */
public class Client {
    protected boolean requestAnswered;
    protected String requestMessage;
    protected int responseNumber;
    protected String serverIP;
    protected int serverPort;

    Client() {
        requestAnswered = false;
        responseNumber = 0;
    }

    public boolean isRequestAnswered() {
        return requestAnswered;
    }

    public void setRequestMessage(String message) {
        requestMessage = message;
    }

    public int getResponseNumber() {
        return responseNumber;
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
