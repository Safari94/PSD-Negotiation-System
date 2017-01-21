package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import org.zeromq.ZMQ;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;


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
    //private final ActorRef publisher;
    public static final int port = 12346;


    public LoginManager(/*ActorRef p*/) {
        //this.publisher=p;
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

        savePedido_to_file(new Pedidos("xavier","joao","Primavera",12, 143));
        savePedido_to_file(new Pedidos("joao","test","Google",12, 143));
        savePedido_to_file(new Pedidos("to","jjj","Amazon",12, 143));

        load_files();


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
            String info;

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
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, users.get(usrname).getUsername()));
                            System.out.println("Mandei mensagem"); //D
                            return true;
                        }
                    } else {
                        System.out.println("User not exists");//D
                        usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));
                        return  true;

                    }



                case SELL:
                    System.out.println();
                    Sell s=(Sell) message.o;



                    if (buys.size()==0){sells.add(s);saveSell_to_file(s); System.out.println(sells.size());}
                    else{

                    for(Buy b:this.buys){

                        if(b.company.equals(s.company)) {
                            if (b.price >= s.price) {
                                float p = (b.price + s.price) / 2;
                                if (b.amount >= s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username, b.company, s.amount, p));
                                    buys.add(new Buy(b.company, (b.amount - s.amount), b.price, b.username));
                                    buys.remove(b);
                                }

                                if (b.amount < s.amount) {
                                    pedidos.add(new Pedidos(s.username, b.username, b.company, s.amount, p));
                                    sells.add(new Sell(s.company, (s.amount - b.amount), s.price, s.username));
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

                    if(sells.size()==0){buys.add(b); saveBuy_to_file(b); System.out.println(buys.size());}
                    else {
                        for (Sell s1 : this.sells) {

                            if (s1.company.equals(b.company)) {
                                if (s1.price >= b.price) {
                                    float p = (s1.price + b.price) / 2;
                                    if (b.amount >= s1.amount) {
                                        pedidos.add(new Pedidos(s1.username, b.username, b.company, s1.amount, p));
                                        savePedido_to_file(new Pedidos(s1.username, b.username, b.company, s1.amount, p));
                                        buys.add(new Buy(b.company, (b.amount - s1.amount), b.price, b.username));
                                        buys.remove(b);
                                    }

                                    if (b.amount < s1.amount) {
                                        pedidos.add(new Pedidos(s1.username, b.username, b.company, s1.amount, p));
                                        sells.add(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username));
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



    //Guardar sells e buys em ficheiros para não haver perdas caso o server va abaixo

    public void load_files(){
        FileInputStream fis = null;
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        try{
            //ficheiro com as compras
            fis = new FileInputStream("buys.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String strLine = "";
            String[] tokens = strLine.split(" ");
            while ((strLine = br.readLine()) != null)   {
                tokens = strLine.split(" ");
                buys.add(new Buy(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis.close();
            //ficheiro com as vendas
            fis1 = new FileInputStream("sells.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            strLine = "";
            tokens = strLine.split(" ");
            while ((strLine = br1.readLine()) != null)   {
                tokens = strLine.split(" ");
                sells.add(new Sell(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis1.close();
            //ficheiro com os pedidos
            fis2 = new FileInputStream("pedidos.txt");
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            strLine = "";
            tokens = strLine.split(" ");
            while ((strLine = br2.readLine()) != null)   {
                tokens = strLine.split(" ");
                pedidos.add(new Pedidos(tokens[0], tokens[1], tokens[2],Integer.parseInt(tokens[3]), Float.parseFloat(tokens[4])));
            }
            fis2.close();
        } catch(FileNotFoundException ex){ System.out.println("FileNotFoundException"); }
        catch(IOException ex){System.out.println("OutputException");}
    }

    public void saveSell_to_file(Sell aux) {
        int n;

        String s = "sells.txt";

        try {
            //SELLS
            PrintWriter pw = new PrintWriter(new FileOutputStream(s));
            n = sells.size();

            aux = sells.get(n+1);
            pw.println(aux.toString());

            pw.close();

        } catch (Exception e) {
        }
    }


    public void saveBuy_to_file(Buy aux) {
        int n;

        String s = "buys.txt";
        String p = "pedidos.txt";
        try {
            //BUYS
            PrintWriter pw = new PrintWriter(new FileOutputStream(s));
            n = buys.size();

            aux = buys.get(n+1);
            pw.println(aux.toString());

            pw.close();

        } catch (Exception e) {
        }
    }

    public void savePedido_to_file(Pedidos aux) {
        int n;


        String p = "pedidos.txt";
        try {
            //BUYS
            PrintWriter pw = new PrintWriter(new FileOutputStream(p));
            n = pedidos.size();

            aux = pedidos.get(n+1);
            pw.println(aux.toString());

            pw.close();

        } catch (Exception e) {
        }
    }



}
