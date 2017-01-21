package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import org.zeromq.ZMQ;

import java.io.*;
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

        saveBuy_to_file(new Buy("ok",69,1,"okwqwqwq"));
        saveSell_to_file(new Sell("ok",69,1,"okwqwqwq"));

        //load_files();


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

                        } else {
                            System.out.println("Password Invalida");//D
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, users.get(usrname).getUsername()));
                            System.out.println("Mandei mensagem"); //D

                        }
                    } else {
                        System.out.println("User not exists");//D
                        usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));


                    }
                    return true;



                case SELL:


                    Sell s=(Sell) message.o;



                    if (buys.size()==0){sells.add(s);saveSell_to_file(s);
                        System.out.println(sells.size());  //D
                    }

                    else{
                        System.out.println("tou aqui");  //D
                        for(Buy b:this.buys){

                            if(b.company.equals(s.company)) {
                                if (b.price >= s.price) {
                                    float p = (b.price + s.price) / 2;
                                    if (b.amount >= s.amount) {
                                        socket.send(new Pedidos(s.username, b.username, b.company, s.amount, p).toString());
                                        buys.add(new Buy(b.company, (b.amount - s.amount), b.price, b.username));
                                        saveBuy_to_file(new Buy(b.company, (b.amount - s.amount), b.price, b.username));
                                        buys.remove(b);
                                        remove_Buy(b);
                                    }

                                    if (b.amount < s.amount) {
                                        socket.send(new Pedidos(s.username, b.username, b.company, s.amount, p).toString());
                                        sells.add(new Sell(s.company, (s.amount - b.amount), s.price, s.username));
                                        saveSell_to_file(new Sell(s.company, (s.amount - b.amount), s.price, s.username));
                                        buys.remove(b);
                                        remove_Buy(b);
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
                        System.out.println("tou aqui");  //D
                        for (Sell s1 : this.sells) {

                            if (s1.company.equals(b.company)) {
                                if (s1.price >= b.price) {
                                    float p = (s1.price + b.price) / 2;
                                    if (b.amount >= s1.amount) {
                                        socket.send(new Pedidos(s1.username, b.username, b.company, s1.amount, p).toString());
                                        System.out.println("enviei");  //D
                                        buys.add(new Buy(b.company, (b.amount - s1.amount), b.price, b.username));
                                        saveBuy_to_file(new Buy(b.company, (b.amount - s1.amount), b.price, b.username));
                                        buys.remove(b);
                                        remove_Buy(b);
                                    }

                                    if (b.amount < s1.amount) {
                                        socket.send(new Pedidos(s1.username, b.username, b.company, s1.amount, p).toString());
                                        sells.add(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username));
                                        saveSell_to_file(new Sell(s1.company, (s1.amount - b.amount), s1.price, s1.username));
                                        buys.remove(b);
                                        remove_Buy(b);
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


    public void saveBuy_to_file(Buy aux) {

        String p = "buys.txt";
        try {
            //Ver se o ficheiro existe
            File f = new File(p);
            if(f.exists() && !f.isDirectory()) {
                FileWriter fw = new FileWriter(p,true);
                //aux = new Pedidos("ok","wtf","google",69,420);
                fw.write(aux.toString()+"\n");
                fw.close();
            }
            else{
                PrintWriter pw = new PrintWriter(new FileOutputStream(p));
                //aux = new Pedidos("oadsdsasda","wtfwqwqwq","googlesasa",69,420454545);
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
                //aux = new Pedidos("ok","wtf","google",69,420);
                fw.write(aux.toString()+"\n");
                fw.close();
            }
            else{
                PrintWriter pw = new PrintWriter(new FileOutputStream(p));
                //aux = new Pedidos("oadsdsasda","wtfwqwqwq","googlesasa",69,420454545);
                pw.println(aux.toString());
                pw.close();
            }
        } catch (Exception e) {
        }
    }


    public void remove_Buy(Buy aux){
        ArrayList<Buy> buys1;
        buys1= new ArrayList<>();
        int n,i;
        String p="buys.txt";
        FileInputStream fis2 = null;
        try{
            //ficheiro com os buys
            fis2 = new FileInputStream(p);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            String strLine = "";
            String[] tokens = strLine.split(" ");
            while ((strLine = br2.readLine()) != null)   {
                tokens = strLine.split(" ");
                buys1.add(new Buy(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis2.close();

            //aux2 = new Pedidos("ok","wtf","google",69,420);
            n = buys1.size();
            Buy aux3;
            for (i=0;i<n;i++) {
                aux3=buys1.get(i);
                //System.out.println(pedidos1.get(i).toString()+i);
                if((aux3.toString()).equals(aux.toString())){
                    System.out.println("OLAAA");
                    buys1.remove(i);
                    n--;
                }
            }

            Buy aux4;
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(p));
            n=buys1.size();
            for (i=0;i<n;i++){
                aux4=buys1.get(i);
                pw2.println(aux4.toString());
            }
            pw2.close();

        } catch(FileNotFoundException ex){ System.out.println("FileNotFoundException"); }
        catch(IOException ex){System.out.println("OutputException");}

    }

    public void remove_Sell(Sell aux){
        ArrayList<Sell> sells1;
        sells1= new ArrayList<>();
        int n,i;
        String p="sells.txt";
        FileInputStream fis2 = null;
        try{
            //ficheiro com os sells
            fis2 = new FileInputStream(p);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
            String strLine = "";
            String[] tokens = strLine.split(" ");
            while ((strLine = br2.readLine()) != null)   {
                tokens = strLine.split(" ");
                sells1.add(new Sell(tokens[0], Integer.parseInt(tokens[1]), Float.parseFloat(tokens[2]), tokens[3]));
            }
            fis2.close();

            //aux2 = new Pedidos("ok","wtf","google",69,420);
            n = sells1.size();
            Sell aux3;
            for (i=0;i<n;i++) {
                aux3=sells1.get(i);
                //System.out.println(pedidos1.get(i).toString()+i);
                if((aux3.toString()).equals(aux.toString())){
                    System.out.println("OLAAA");
                    sells1.remove(i);
                    n--;
                }
            }

            Sell aux4;
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(p));
            n=sells1.size();
            for (i=0;i<n;i++){
                aux4=sells1.get(i);
                pw2.println(aux4.toString());
            }
            pw2.close();

        } catch(FileNotFoundException ex){ System.out.println("FileNotFoundException"); }
        catch(IOException ex){System.out.println("OutputException");}

    }




}
