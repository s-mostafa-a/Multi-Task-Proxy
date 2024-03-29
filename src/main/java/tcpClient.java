import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by ahmadi on 7/14/18.
 */
public class tcpClient extends Client {
    tcpClient() {
        super();
    }

    @Override
    public void run() {
        String serverResponse = "";
        try {
            Socket clientSocket = new Socket(serverIP, serverPort);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(requestMessage + "\n");
            outToServer.flush();
            String line;
            while((line = inFromServer.readLine())!=null)
                serverResponse = serverResponse + line + "\n";
            System.out.println("response from server:\n" + serverResponse);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
