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
            + "1. Notify by Enterpise.\n2. Notify by amount.\n3. Notify by price c\n"
            + "4. All\n5. I have already chosen the notifications that interest me.\n"
            + "6. Reset\n7. Logout\n\n0. Exit\n==========================\n";

    private static Scanner input = new Scanner(System.in);
    private static Socket server               = null;
    private static BufferedReader fromServer   = null;
    private static BufferedWriter toServer     = null;
    private static boolean logged              = false;
    private static ArrayList<String> notifTypes;



    private static int login() throws IOException{
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
                toServer.write("LOGIN " + user +" "+ pass + "\n");
                toServer.flush();

                servrep = fromServer.readLine();
                String[] message = servrep.split(" ");
                if(message[0].equals("welcome") ){
                    if(message.length > 2){
                        logged = true;
                        System.out.println("\nWelcome, "+message[2]);

                    }
                    else{
                        System.out.println("\nUser is not an admin.");
                        toServer.write("logout \n");
                        toServer.flush();
                        fromServer.readLine();
                    }

                }
                else{
                    System.out.println("\nInvalid name or password.");
                }
                return 1;
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


        System.out.println("==========================\n"+mainMenu
                +"\n==========================\n");
        option = input.nextLine();

        switch(option){
            case "1":
                notifTypes.add("info:Enterprise");
                break;
            case "2":
                notifTypes.add("info:Amount");
                break;
            case "3":
                notifTypes.add("info:Price");
                break;

            case "4":
                notifTypes.add("info");
                break;
            case "5":
                subscribe(notifTypes);
                return 1;
            case "6":
                notifTypes.clear();
                break;
            case "7":
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

    public static void subscribe(ArrayList<String> types){
        byte[] received;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://localhost:" + 12346);

        System.out.println("Connected");

        for(String s : types){
            System.out.println("Subscribing to "+s);
            socket.subscribe(s.getBytes());
        }
        while (true) {
            received = socket.recv();
            System.out.print(new String(received));
        }

        //socket.close();
        //context.term();
    }

    public static void main(String args[]) throws IOException {

        server = new Socket("localhost", 12345);
        fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        toServer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        boolean exitflag = false;
        notifTypes = new ArrayList<>();

        System.out.println("==== Notification Console ====\nWelcome\n");

        while(!exitflag){
            if(login()==-1)
                exitflag = true;
            while(logged){
                if(consoleMenu()==-1)
                    exitflag = true;
            }
        }
    }
}
