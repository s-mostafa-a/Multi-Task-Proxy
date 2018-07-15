import org.xbill.DNS.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by ahmadi on 7/15/18.
 */
public class tcpClientsProxyServerThread extends Thread {
    Socket socket;

    public tcpClientsProxyServerThread(Socket socket) {
        super();
        this.socket = socket;
    }


    @Override
    public void run() {
        String clientEntry;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            clientEntry = reader.readLine();

            // processing string
            clientEntry = clientEntry.toLowerCase();
            char[] clientEntryChar = clientEntry.toCharArray();
            int indexOfType = clientEntry.indexOf("type=");
            int indexOfServer = clientEntry.indexOf("server=");
            int indexOfTarget = clientEntry.indexOf("target=");
            String type = "";
            String server = "";
            String target = "";
            for (int i = indexOfType + 5; i < clientEntry.length(); i++){
                if (clientEntryChar[i] == ' ')
                    break;
                type = type + clientEntryChar[i];
            }
            for (int i = indexOfServer + 7; i < clientEntry.length(); i++){
                if (clientEntryChar[i] == ' ')
                    break;
                server = server + clientEntryChar[i];
            }
            for (int i = indexOfTarget + 7; i < clientEntry.length(); i++){
                if (clientEntryChar[i] == ' ')
                    break;
                target = target + clientEntryChar[i];
            }
            //
            int intType;
            if (type.compareTo("a")==0)
                intType = Type.A;
            else
                intType = Type.CNAME;
            String result = resolve(target, intType, server);
            outputStream.writeBytes(result);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

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
