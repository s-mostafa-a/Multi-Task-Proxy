import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by ahmadi on 7/14/18.
 */
public class udpClient extends Client {
    private char desiredSeqNum;
    private InetAddress ServerIPAddress;
    private DatagramSocket clientSocket;

    udpClient() {
        super();
        desiredSeqNum = '0';
    }

    //request will not be segmented!
    @Override
    public void run() {
        String serverResponse = "";
        try {
            clientSocket = new DatagramSocket();
            //TODO
            ServerIPAddress = InetAddress.getByName(serverIP);
            byte[] sendData;
            byte[] receiveData = new byte[65535];
            String sentence = requestMessage;
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ServerIPAddress, serverPort);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket;
            String modifiedSentence;
            char[] chars;
            do {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                modifiedSentence = new String(receivePacket.getData());
                chars = modifiedSentence.toCharArray();
                if (desiredSeqNum == chars[1]) {
                    desiredSeqNum = (((Integer.parseInt("" + desiredSeqNum) + 1) % 2) + "").toCharArray()[0];
                    serverResponse = serverResponse + modifiedSentence.substring(2, modifiedSentence.length() - 1);
                }
                sendAck(desiredSeqNum);
            } while (chars[0] != '0');
            System.out.println("response from server:\n" + serverResponse);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAck(char desiredSeqNum) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(("Waiting for " + desiredSeqNum).getBytes(), (desiredSeqNum + "").getBytes().length, ServerIPAddress, serverPort);
        clientSocket.send(sendPacket);
    }
}
