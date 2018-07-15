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
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(sourcePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server listening on port " + sourcePort + "!");
        while (true) {
            Socket socket = null;
            try {
                socket = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new tcpClientsProxyServerThread(socket).start();
        }
    }

}
