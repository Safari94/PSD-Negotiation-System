package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * Created by xavier on 29/12/16.
 */
public class LoginManager extends BasicActor<Message,Void> {

    private HashMap<String, ClientInfo> users;

    private ArrayList<Sell> sells;
    private ArrayList<Buy> buys;
    private LinkedList<Pedidos> pedidos;
    private ZMQ.Context context;
    private ZMQ.Socket socket;
    public static final int port = 12346;


    public LoginManager() {

        this.users = new HashMap<>();
        this.context = ZMQ.context(1);
        this.socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:" + port);

        this.sells = new ArrayList<>();
        this.buys = new ArrayList<>();
        this.pedidos = new LinkedList<Pedidos>();
    }

    private void populate() {

        ClientInfo jjj = new ClientInfo("jjj", "jjj");
        ClientInfo xavier = new ClientInfo("xavier", "xavier");
        ClientInfo test = new ClientInfo("test", "test");
        ClientInfo to = new ClientInfo("to", "to");

        users.put("xavier", xavier);
        users.put("jjj", jjj);
        users.put("test", test);
        users.put("to", to);


    }

    public void sendPedidos() throws InterruptedException {

        while (true) {
            if (pedidos.size() > 0) {

                socket.send(pedidos.getFirst().toString());
                pedidos.remove(0);
            } else {
                Thread.sleep(10);
            }


        }

    }

    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {

        populate();


        while (receive(message -> {

            switch (message.type) {
                case LOGIN:
                     ClientInfo usrinfo = (ClientInfo) message.o;
                     String usrname = usrinfo.getUsername();

                    if (users.containsKey(usrname)) {
                        if (users.get(usrname).authenticate(usrinfo.getPassword())) {

                            usrinfo.getActor().send(new Message(Type.LOGIN_OK, users.get(usrname).getUsername()));
                            return true;
                        } else {
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, usrname));
                            return true;

                        }
                    } else {
                        usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));

                    }
                        break;



                case SELL:

                    Sell s=(Sell) message.o;
                    s.cli.send(new Message(Type.SELL_OK,null ));

                    for(Buy b:this.buys){

                        if(b.company.equals(s.company)){
                            if(b.price>=s.price){
                                float p= (b.price+s.price)/2;
                                if(b.amount>=s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username,b.company,s.amount, p));
                                    buys.add(new Buy(b.company,(b.amount-s.amount),b.price,b.username,null));
                                    buys.remove(b);
                                }

                                if(b.amount<s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username,b.company,s.amount, p));
                                    sells.add(new Sell(s.company,(s.amount-b.amount),s.price,s.username,null));
                                    buys.remove(b);
                                }



                            }
                        }







                    }
                    sendPedidos();

                    return true;
                case BUY:

                    Buy b=(Buy) message.o;
                    System.out.println("AQUI11");

                    System.out.println("AQUI22");

                    System.out.println("AQUI33");
                    b.cli.send(new Message(Type.BUY_OK, null));
                    System.out.println("AQUI2");
                    if(sells.size()==0){buys.add(b);}
                    else {
                        for (Sell s1 : this.sells) {

                            if (s1.company.equals(b.company)) {
                                if (s1.price >= b.price) {
                                    float p = (s1.price + b.price) / 2;
                                    if (b.amount >= s1.amount) {
                                        pedidos.add(new Pedidos(s1.username, b.username, b.company, s1.amount, p));
                                        buys.add(new Buy(b.company, (b.amount - s1.amount), b.price, b.username,null));
                                        buys.remove(b);
                                    }

                                    if (b.amount < s1.amount) {
                                        pedidos.add(new Pedidos(s1.username, b.username, b.company, s1.amount, p));
                                        sells.add(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username,null));
                                        buys.remove(b);
                                    }


                                }
                            }


                        }
                    }

                    sendPedidos();





                    return true;


        }
        return null;
    }));

        return null;
}



}