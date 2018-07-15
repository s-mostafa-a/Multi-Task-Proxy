import java.io.IOException;

/**
 * Created by ahmadi on 7/14/18.
 */
public class ProxyServer {
    protected String sourceIP;
    protected int sourcePort;

    public ProxyServer(String sourceIP, int sourcePort) {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
    }

    public void run() throws IOException {
    }

    private String resolve() {
        return "";
    }
}
