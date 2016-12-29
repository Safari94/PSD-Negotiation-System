package Exchange;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import org.zeromq.ZMQ;
import Exchange.Message.Type;

import java.net.Socket;
import java.util.*;


/**
 * Created by xavier on 29/12/16.
 */
public class RequestManager extends BasicActor<Message, Void> {


    private ArrayList<Sell> sells;
    private ArrayList<Buy> buys;
    private LinkedList<Pedidos> pedidos;
    private ZMQ.Context context;
    private ZMQ.Socket socket;
    public static final int port = 12347;

    public RequestManager() {

        this.context = ZMQ.context(1);
        this.socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + port);

        this.sells = new ArrayList<>();
        this.buys = new ArrayList<>();
        this.pedidos = new LinkedList<Pedidos>();
    }




    public void sendPedidos() throws InterruptedException {

        while(true){
            if(pedidos.size()>0){

                socket.send(pedidos.getFirst().toString());
                pedidos.remove(0);
            }
            else{
                Thread.sleep(10);
            }



        }


    }



    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {

        sendPedidos();

        while (receive(message -> {

            switch (message.type) {
                case SELL:
                    Sell s=(Sell) message.o;

                    for(Buy b:this.buys){

                        if(b.company.equals(s.company)){
                            if(b.price>=s.price){
                                float p= (b.price+s.price)/2;
                                if(b.amount>=s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username,b.company,s.amount, p));
                                    buys.add(new Buy(b.company,(b.amount-s.amount),b.price,b.username));
                                    buys.remove(b);
                                }

                                if(b.amount<s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username,b.company,s.amount, p));
                                    sells.add(new Sell(s.company,(s.amount-b.amount),s.price,s.username));
                                    buys.remove(b);
                                }



                            }
                        }





                    }

                    return true;
                case BUY:
                    Buy b=(Buy) message.o;

                    for(Sell s1:this.sells){

                        if(s1.company.equals(b.company)){
                            if(s1.price>=b.price){
                                float p= (s1.price+b.price)/2;
                                if(b.amount>=s1.amount) {
                                    pedidos.add(new Pedidos(s1.username, b.username,b.company,s1.amount, p));
                                    buys.add(new Buy(b.company,(b.amount-s1.amount),b.price,b.username));
                                    buys.remove(b);
                                }

                                if(b.amount<s1.amount) {
                                    pedidos.add(new Pedidos(s1.username, b.username,b.company,s1.amount, p));
                                    sells.add(new Sell(s1.company,(s1.amount-b.amount),s1.price,s1.username));
                                    buys.remove(b);
                                }



                            }
                        }





                    }





                    return true;

            }
            return null;
        })) ;

        return null;
    }


}

























