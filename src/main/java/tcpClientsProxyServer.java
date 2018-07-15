import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ahmadi on 7/14/18.
 */
public class tcpClientsProxyServer extends ProxyServer {
    public tcpClientsProxyServer(String sourceIP, int sourcePort) {
        super(sourceIP, sourcePort);
    }

    @Override
    public void run() throws IOException {
        ServerSocket server = new ServerSocket(sourcePort);
        System.out.println("Server listening on port " + sourcePort + "!");
        while (true) {
            Socket socket = server.accept();
            new tcpClientsProxyServerThread(socket).start();
        }
    }

}
