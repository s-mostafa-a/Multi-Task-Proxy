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
    private boolean finished = false;
    private TimerCounter timerCounter;

    udpClient() {
        super();
        desiredSeqNum = '0';
    }

    public boolean isFinished() {
        return finished;
    }

    //request will not be segmented!
    @Override
    public void run() {
        String serverResponse = "";
        try {
            clientSocket = new DatagramSocket();
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
            timerCounter = new TimerCounter(this);
            timerCounter.start();
            do {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                modifiedSentence = new String(receiveData, 0, receiveData.length);
                chars = modifiedSentence.toCharArray();
                if (desiredSeqNum == chars[1]) {
                    desiredSeqNum = (((Integer.parseInt("" + desiredSeqNum) + 1) % 2) + "").toCharArray()[0];
                    serverResponse = serverResponse + modifiedSentence.substring(2, modifiedSentence.indexOf('\u0000'));
                }
                sendAck();
            } while (chars[0] != '0');
            System.out.println("response from server:\n" + serverResponse);
            finished = true;
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAck() throws IOException {
        DatagramPacket sendPacket = new DatagramPacket((desiredSeqNum + "").getBytes(), (desiredSeqNum + "").getBytes().length, ServerIPAddress, serverPort);
        clientSocket.send(sendPacket);
        timerCounter.reset();
    }
}
