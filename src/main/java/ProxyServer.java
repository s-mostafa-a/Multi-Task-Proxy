import java.io.IOException;
/**
 * Created by ahmadi on 7/14/18.
 */
public class ProxyServer {
    protected String sourceIP;
    protected String sourcePort;

    public ProxyServer(String sourceIP, String sourcePort) {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
    }

    public void run() throws IOException {
    }

    private String resolve() {
        return "";
    }
}
