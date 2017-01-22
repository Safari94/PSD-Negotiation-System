package Exchange;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import org.zeromq.ZMQ;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by xavier on 29/12/16.
 */
public class LoginManager extends BasicActor<Message,Void> {

    private HashMap<String, ClientInfo> users;

    private ArrayList<Sell> sells;
    private ArrayList<Buy> buys;

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

        load_files();
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

                        } else {
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, users.get(usrname).getUsername()));

                        }
                    } else {
                        usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));


                    }
                    return true;



                case SELL:


                    Sell s=(Sell) message.o;



                    if (buys.size()==0){sells.add(s);saveSell_to_file(s);
                    }

                    else{
                        for(Buy b:this.buys){

                            if(b.company.equals(s.company)) {
                                if (b.price >= s.price) {
                                    float p = (b.price + s.price) / 2;
                                    if (b.amount >= s.amount) {
                                        socket.send(new Pedidos(s.username, b.username, b.company, s.amount, p).toString());
                                        buys.add(new Buy(b.company, (b.amount - s.amount), b.price, b.username));
                                        saveBuy_to_file(new Buy(b.company, (b.amount - s.amount), b.price, b.username));
                                        buys.remove(b);
                                        save_to_file();
                                    }

                                    if (b.amount < s.amount) {
                                        socket.send(new Pedidos(s.username, b.username, b.company, s.amount, p).toString());
                                        sells.add(new Sell(s.company, (s.amount - b.amount), s.price, s.username));
                                        saveSell_to_file(new Sell(s.company, (s.amount - b.amount), s.price, s.username));
                                        buys.remove(b);
                                        save_to_file();
                                    }


                                }
                            }
                        }

                    }

                    return true;


                case BUY:
                    Buy b=(Buy) message.o;

                    if(sells.size()==0){buys.add(b);saveBuy_to_file(b);  System.out.println(buys.size());}
                    else {
                        for (Sell s1 : this.sells) {

                            if (s1.company.equals(b.company)) {
                                if (s1.price >= b.price) {
                                    float p = (s1.price + b.price) / 2;
                                    if (b.amount >= s1.amount) {
                                        socket.send(new Pedidos(s1.username, b.username, b.company, s1.amount, p).toString());
                                        buys.add(new Buy(b.company, (b.amount - s1.amount), b.price, b.username));
                                        saveBuy_to_file(new Buy(b.company, (b.amount - s1.amount), b.price, b.username));
                                        buys.remove(b);
                                        save_to_file();
                                    }

                                    if (b.amount < s1.amount) {
                                        socket.send(new Pedidos(s1.username, b.username, b.company, s1.amount, p).toString());
                                        sells.add(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username));
                                        saveSell_to_file(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username));
                                        buys.remove(b);
                                        save_to_file();
                                    }
                                }
                            }
                        }
                    }

                    return true;

            }
            return null;
        }));
        return null;
    }

    public void load_files(){
        FileInputStream fis = null;
        FileInputStream fis1 = null;
        String[] tokens;
        String strLine;
        try{
            //Compras
            fis = new FileInputStream("buys.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while ((strLine = br.readLine()) != null)   {
                tokens = strLine.split(" ");
                buys.add(new Buy(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis.close();
            //Vendas
            fis1 = new FileInputStream("sells.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
            while ((strLine = br1.readLine()) != null)   {
                tokens = strLine.split(" ");
                sells.add(new Sell(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis1.close();
        } catch (Exception ex){
            System.out.println("File or Output Exception");
        }

    }

    public void save_to_file(){
        int n,i;
        Sell aux;
        Buy aux1;
        String b="buys.txt";
        String s="sells.txt";
        try{
            //SELLS
            PrintWriter pw = new PrintWriter(new FileOutputStream(s));
            n=sells.size();
            for (i=0;i<n;i++){
                aux=sells.get(i);
                pw.println(aux.toString());
            }
            pw.close();
            //BUYS
            PrintWriter pw1 = new PrintWriter(new FileOutputStream(b));
            n=buys.size();
            for (i=0;i<n;i++){
                aux1=buys.get(i);
                pw1.println(aux1.toString());
            }
            pw1.close();
        }catch (Exception e){}
    }


    public void saveBuy_to_file(Buy aux) {

        String p = "buys.txt";
        try {
            //Ver se o ficheiro existe
            File f = new File(p);
            if(f.exists() && !f.isDirectory()) {
                FileWriter fw = new FileWriter(p,true);
                fw.write(aux.toString()+"\n");
                fw.close();
            }
            else{
                PrintWriter pw = new PrintWriter(new FileOutputStream(p));
                pw.println(aux.toString());
                pw.close();
            }
        } catch (Exception e) {
        }
    }

    public void saveSell_to_file(Sell aux) {

        String p = "sells.txt";
        try {
            //Ver se o ficheiro existe
            File f = new File(p);
            if(f.exists() && !f.isDirectory()) {
                FileWriter fw = new FileWriter(p,true);
                fw.write(aux.toString()+"\n");
                fw.close();
            }
            else{
                PrintWriter pw = new PrintWriter(new FileOutputStream(p));
                pw.println(aux.toString());
                pw.close();
            }
        } catch (Exception e) {
        }
    }

}
