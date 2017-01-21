package Client;

import java.io.*;
import java.util.*;
import org.zeromq.ZMQ;
import java.net.Socket;

/**
 * Created by xavier on 11/01/17.
 */
public class Subscriber {


    private static final  String loginMenu = "\n1.Login\n0.Exit\n";
    private static final String mainMenu = "==========================\n"
    + "1. Subscribe to enterprise notifications.\n2. Unsubscribe from enterprise notifications.\n3. Validate my choices.\n4. List subscriptions\n"
    + "\n5. Logout\n\n0. Exit\n==========================\n";

    private static Scanner input = new Scanner(System.in);
    private static Socket server               = null;
    private static BufferedReader fromServer   = null;
    private static BufferedWriter toServer     = null;
    private static boolean logged              = false;
    private static ArrayList<String> subscriptions;
    private static ArrayList<String> unsubscriptions;
    private static ArrayList<String> subscribed;
    private static boolean FirstTime = true;
    private static byte[] received;
    private static ZMQ.Context context;
    private static ZMQ.Socket socket;



    public int login() throws IOException{
        String user, pass, option;
        String servrep;
        System.out.println(loginMenu);
        option = input.nextLine();

        switch (option) {
            case "1":
                System.out.println("Username: ");
                user = input.nextLine();
                System.out.println("Password: ");
                pass = input.nextLine();
                toServer.write("login " + user +" "+ pass + "\n");
                toServer.flush();

                servrep = fromServer.readLine();
                String[] message = servrep.split(" ");
                if(message[0].equals("welcome") ){
                    logged = true;
                    System.out.println("\nWelcome, " + message[1]);
                        
                    return 1;

                }
                else{
                    System.out.println("\nInvalid name or password.");
                }
                return 0;
            case "0":
                toServer.write("exit\n");
                toServer.flush();
                return -1;
            default :
                System.out.println("\nInvalid option.\nPlease choose one of the options presented");
                break;
        }
        return 0;
    }

    private static int consoleMenu(){
        String option;
        String company;

        System.out.println("==========================\n"+mainMenu
                +"\n==========================\n");
        option = input.nextLine();

        switch(option){
            case "1":
                if (subscribed.size() + subscriptions.size() >= 10){
                    System.out.println("You reached the maximum number of 10 subscriptions.");
                }else{
                    System.out.println("Please Enter the enterprise Name to subscribe:");
                    company = input.nextLine();
                    if (!checkExists("info:" + company)){
                        subscriptions.add("info:" + company);
                    }else{
                        System.out.println("You cannot subscribe twice to the same company.");
                    }
                }
                break;
            case "2":
                System.out.println("Please Enter the enterprise Name to unsubscribe:");
                company = input.nextLine();
                if(checkExists("info:" + company)){
                    unsubscriptions.add("info:" + company);
                }else{
                    System.out.println("You are not subscribed to this company.");
                }
                break;
            case "3":
                if (FirstTime)
                    createSocket();
                subscribe(subscriptions, unsubscriptions);
                break;
            case "4":
                displayChoices(subscribed);
                break;
            case "5":
                logged = false;
                return 0;
            case "0":
                logged = false;
                return -1;
            default :
                System.out.println("\nInvalid option.\nPlease choose one of the options presented");
                break;
        }
        return 0;
    }
    
    public static boolean checkExists(String name){
        boolean check = false;
        for (int i = 0; i < subscribed.size(); i++){
            if(subscribed.get(i).equals(name))
                check = true;
        }
        return check;
            
    }

    public static void displayChoices(ArrayList<String> subscribedList){
        if(subscribedList.size() == 0){
            System.out.println("You have no subscriptions.");
        }else{
            System.out.println("You are subscribed to:");
            for(String s : subscribedList){
                System.out.println("\n- "+s.substring(5));
            }
        }
    }
    
    public static void subscribe(ArrayList<String> subs, ArrayList<String> unsubs){        
        for(String s : subs){
            System.out.println("Subscribing to "+s.substring(5));
            socket.subscribe(s.getBytes());
            subscribed.add(s);
        }
        subscriptions.clear();
        
        for(String s : unsubs){
            System.out.println("Unsubcribing from "+s.substring(5));
            socket.unsubscribe(s.getBytes());
            searchAndRemove(s);
        }
        unsubscriptions.clear();
    }
    
    public static void searchAndRemove(String s){
        int index = 0;
        for(int i = 0; i < subscribed.size(); i++){
            if(subscribed.get(i).equals(s))
                index = i;
        }
        subscribed.remove(index);
    }
    
    public static void createSocket(){
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://localhost:" + 12347);
        
        System.out.println("Connected");
        
        FirstTime = false;
        
        new Thread(new Runnable()
                   {
            public void run()
            {
                while (!Thread.currentThread().isInterrupted()) {
                    received = socket.recv();
                    System.out.print(new String(received).substring(5));
                }
            } 
        }).start();
    }
    
    

    public static void main(String args[]) throws IOException {

        server = new Socket("localhost", 12345);
        fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        toServer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        boolean exitflag = false;
        subscriptions = new ArrayList<>();
        unsubscriptions = new ArrayList<>();
        subscribed = new ArrayList<>();
        Subscriber sb= new Subscriber();

        System.out.println("==== Notification Console ====\nWelcome\n");

        while(!exitflag){
            if(sb.login()==-1)
                exitflag = true;
            while(logged){
                sb.consoleMenu();
            }
        }
    }
}
