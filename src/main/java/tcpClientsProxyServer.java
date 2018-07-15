import org.xbill.DNS.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by ahmadi on 7/14/18.
 */
public class tcpClientsProxyServer extends ProxyServer {
    public tcpClientsProxyServer(String sourceIP, String sourcePort) {
        super(sourceIP, sourcePort);
    }

    @Override
    public void run() throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server listening on port " + sourcePort + "!");
        while (true) {
            Socket socket = server.accept();
            new tcpClientsProxyServerThread(socket).start();
        }
    }

    //works fine
    private String resolve(String host, int type, String DNSServer) {
        try {
            Lookup lookup = new Lookup(host, type);
            SimpleResolver resolver = new SimpleResolver(DNSServer);
            resolver.setTimeout(5);
            lookup.setResolver(resolver);
            Record[] result = lookup.run();
            if (result == null) return null;
            List<Record> records = java.util.Arrays.asList(result);
            java.util.Collections.shuffle(records);
            String response = "";
            for (Record record : records) {
                if (type == Type.A) {
                    response = response + ((ARecord) record).getAddress().getHostAddress() + "\n";
                } else if (type == Type.CNAME) {
                    response = response + ((CNAMERecord) record).getTarget() + "\n";
                }
            }
            return response;
        } catch (Exception ex) {
            return "";
        }
    }
}
