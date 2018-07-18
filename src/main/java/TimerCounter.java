import java.io.IOException;

/**
 * Created by ahmadi on 7/17/18.
 */
public class TimerCounter extends Thread {
    udpClient mycl;
    private int number;
    private final int timePiece = 100;
    private final int maxNumber = 1;

    TimerCounter(udpClient mycl) {
        this.mycl = mycl;
        number = 0;
    }

    public void reset() {
        number = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(timePiece);
                if(!(mycl.getServerResponse().compareTo("")==0))
                    number++;
                if (mycl.isFinished())
                    break;
                if (number == maxNumber) {
                    mycl.sendAck();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
