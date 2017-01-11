package Settlement;

import org.zeromq.ZMQ;
import Bank.Bank;


/**
 * Created by xavier on 30/12/16.
 */
public class ServerSettlement {

    private static final ZMQ.Context context = ZMQ.context(1);
    private static  final ZMQ.Socket socket= context.socket(ZMQ.SUB);

    public static void main(String[] args) throws Exception {

        int port = 12346;
        socket.connect("tcp://localhost:" + port);
        socket.subscribe("".getBytes());

        while (true) {
            byte[] b = socket.recv();
            String mess = new String(b);
            System.out.println(mess);

            new Bank(mess).spawn();
        }
        //socket.close();
        //context.term();
    }
}
