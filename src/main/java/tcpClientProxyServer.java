import org.xbill.DNS.*;

import java.util.List;

/**
 * Created by ahmadi on 7/14/18.
 */
public class tcpClientProxyServer extends ProxyServer {
    public tcpClientProxyServer(String sourceIP, String sourcePort) {
        super(sourceIP, sourcePort);
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
