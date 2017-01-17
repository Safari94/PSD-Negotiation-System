package Settlement;

import org.zeromq.ZMQ;
import Bank.Bank;


/**
 * Created by xavier on 30/12/16.
 */
public class ServerSettlement {

    private static final ZMQ.Context context = ZMQ.context(1);
    private static  final ZMQ.Socket socket= context.socket(ZMQ.SUB);

    public static void main(String[] args) {

        int port = 12345;
        System.out.println("Settlement Iniciado!");

        try {
            //socket.connect("tcp://localhost:" + port);
            //socket.subscribe("".getBytes());
            //while (true) {
                //byte[] b = socket.recv();
                //String mess = new String(b);
                /*Debugging / Testing do Settlement
                String mess = "comprador vendedor empresa quantidade preço";
                */
                String mess = "to xavier primavera 25 2";
                //String mess = "joao jjj eurotux 5 10";

                System.out.println("A testar : "+mess);

                new Bank(mess).spawn();
            //}
        } catch (Exception e){
            //socket.close();
            //context.term();
        }
    }
}
