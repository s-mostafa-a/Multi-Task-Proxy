import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by ahmadi on 7/15/18.
 */
public class tcpClientsProxyServerThread extends Thread{
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
            }
        catch (Exception e){

        }
    }


}
