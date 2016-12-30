package Settlement;

import org.zeromq.ZMQ;

/**
 * Created by xavier on 30/12/16.
 */
public class ServerSettlement {

    private static final ZMQ.Context context = ZMQ.context(1);


    public static void main(String[] args) throws Exception {


        int port = 12347;
        ZMQ.Socket socket = context.socket(ZMQ.SUB);

        socket.connect("tcp://localhost:" + port);
        socket.connect("inproc://noti");





        while (true) {

            byte[] b = socket.recv();
            String mess = new String(b);

            String[] aux = mess.split(" ");




        }
        //socket.close();
        //context.term();
    }
}
