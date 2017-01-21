package Exchange;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.zeromq.ZMQ;

/**
 * Publisher Actor - Receives messages from the server and sends them through a ZeroMQ socket to it's subscribers
 *
 */

public class Publisher extends BasicActor<Message, Void> {
    private ZMQ.Socket socket = null;
    private final int port = 12347;

    public Publisher() {
        ZMQ.Context context = ZMQ.context(1);
        socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + port);
        System.out.println("Creating Publisher");//D
    }

    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {
        System.out.println("Entering While waiting for message Pub...");//D

        while (receive(msg -> {
            System.out.println("I received something : " + msg.o);//D
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            this.socket.send(msg.o + " [" + date + "]\n");
            return true;
        }));
        System.out.println("Leaving While...");//D

        return null;
    }

}
