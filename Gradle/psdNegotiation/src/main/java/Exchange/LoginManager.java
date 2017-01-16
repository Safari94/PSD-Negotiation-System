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

        pedidos.add(new Pedidos("xavier","joao","Primavera",12, 143));
        pedidos.add(new Pedidos("joao","test","Google",12, 143));
        pedidos.add(new Pedidos("jjj","to","IBM",12, 143));
        pedidos.add(new Pedidos("to","jjj","Amazon",12, 143));


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
        System.out.println("tou aqui");
        populate();
        while (receive(message -> {

            switch (message.type) {

                case LOGIN:
                    System.out.println("recebi login"); // D
                    ClientInfo usrinfo = (ClientInfo) message.o;
                    String usrname = usrinfo.getUsername();
                    System.out.println("Vou verificar."+usrname); //D
                    if (users.containsKey(usrname)) {
                        System.out.println("User existe"); //D
                        if (users.get(usrname).authenticate(usrinfo.getPassword())) {
                            System.out.println("Mandei mensagem"); //D
                            usrinfo.getActor().send(new Message(Type.LOGIN_OK, users.get(usrname).getUsername()));
                            return true;
                        } else {
                            System.out.println("Password Invalida");//D
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, usrname));
                            return true;
                        }
                    } else {
                        System.out.println("User not exists");//D
                        usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));

                    }
                    break;


                case SELL:
                    System.out.println();
                    Sell s=(Sell) message.o;


                    if (buys.size()==0){sells.add(s); System.out.println(sells.size());}
                    else{

                    for(Buy b:this.buys){

                        if(b.company.equals(s.company)) {
                            if (b.price >= s.price) {
                                float p = (b.price + s.price) / 2;
                                if (b.amount >= s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username, b.company, s.amount, p));
                                    buys.add(new Buy(b.company, (b.amount - s.amount), b.price, b.username, null));
                                    buys.remove(b);
                                }

                                if (b.amount < s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username, b.company, s.amount, p));
                                    sells.add(new Sell(s.company, (s.amount - b.amount), s.price, s.username, null));
                                    buys.remove(b);
                                }


                            }
                        }
                    }

                    }
                    sendPedidos();
                    break;


                case BUY:
                    Buy b=(Buy) message.o;

                    if(sells.size()==0){buys.add(b); System.out.println(buys.size());}
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
                    break;

            }
        return null;
        }));
        return null;
    }
}