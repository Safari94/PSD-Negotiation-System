package Client;

/**
 * Created by xavier on 29/12/16.
 */

import org.zeromq.ZMQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client_Main {

    private final String loginMenu ;
    private  String userName;
    private final String usermenu = "\n1.Buy \n2.Sell \n0.Logout\n";
    private boolean logged = false;
    private Scanner input = new Scanner(System.in);
    private Socket clientsock = null;
    BufferedWriter toServer = null;
    BufferedReader fromServer = null;


    /*
    Constructor
    */
    public Client_Main( Socket client) throws IOException{
        this.clientsock = client;
        fromServer = new BufferedReader(new InputStreamReader(clientsock.getInputStream()));
        toServer = new BufferedWriter(new OutputStreamWriter(clientsock.getOutputStream()));
        this.userName=new String();
        loginMenu="1.Login\n0.Exit\n";
    }


    public boolean isLogged(){
        return this.logged;
    }

    /**
     * Method for handling login operation.
     * @throws IOException
     */
    public int login() throws IOException{

        String user, pass, option;
        String servrep;
        System.out.println("==========================\n"
                + loginMenu +"\n==========================\n");
        option = input.nextLine();

        if (option.equals("1")) {
            System.out.println("Username: ");
            user = input.nextLine();
            System.out.println("Password: ");
            pass = input.nextLine();
            sendCommand("login", user + " " + pass);

            servrep = fromServer.readLine();
            String[] message = servrep.split(" ");
            if (message[0].equals("welcome")) {

                this.logged = true;
                this.userName=message[1];
                System.out.println("\nWelcome, " + message[1]);

                return 1;
            } else {
                System.out.println("\nInvalid name or password.");
            }
            return 0;
        } else if (option.equals("0")) {
            sendCommand("exit", "");
            return -1;
        } else {
            System.out.println("\nInvalid option.\nPlease choose one of the options presented");

        }
        return 0;
    }

    /**
     * Method for handling replies from the Server to an User.
     * @param servout
     * @throws IOException
     */
    public void handleServerReplyU(String servout) throws IOException{
        String[] srep = servout.split(" ");
        System.out.println(servout);

     if (srep[0].equals("welcome")) {
            this.logged = true;
            System.out.println("\nWelcome, " + srep[1]);

        } else if (srep[0].equals("out")) {
            this.logged = false;
            System.out.println("\nSuccessfully logged out");



        } else if (srep[0].equals("login_failed1")) {
            System.out.println("\nUser " + srep[1] + " doesn't exist or wrong password");

        }
    }



    /**
     * Shows User Menu and handles each option.
     *
     * @return 0 if everything worked fine, -1 if user wants to exit.
     * @throws IOException
     */
    public void userMenu() throws IOException {
        String option, company, amount, price, servout;
        String[] srep;
        while (isLogged()) {
            System.out.println("==========================\n"
                    + usermenu + "\n==========================\n");
            option = input.nextLine();

            if (option.equals("1")) {
                System.out.println("Empresa:");
                company = input.nextLine();
                System.out.println("Quantidade:");
                amount = input.nextLine();
                System.out.println("Preço:");
                price = input.nextLine();
                System.out.println("buy " + company + " " + amount + " " + price + " " + this.userName);

                sendCommand("buy", company + " " + amount + " " + price + " " + this.userName);

                handleServerReplyU(fromServer.readLine());

                while ((servout = fromServer.readLine()) != null && (!servout.equals("")) && (!servout.equals("\n"))) {
                    System.out.println(servout);
                }

            } else if (option.equals("2")) {
                System.out.println("Empresa:");
                company = input.nextLine();
                System.out.println("Quantidade:");
                amount = input.nextLine();
                System.out.println("Preço:");
                price = input.nextLine();
                System.out.println("sell " + company + " " + amount + " " + price + " " + this.userName);

                sendCommand("sell", company + " " + amount + " " + price + " " + this.userName);


                handleServerReplyU(fromServer.readLine());


                while ((servout = fromServer.readLine()) != null && (!servout.equals("")) && (!servout.equals("\n"))) {
                    System.out.println(servout);
                }


            } else if (option.equals("0")) {
                sendCommand("out", "");
                handleServerReplyU(fromServer.readLine());

            }

        }
    }


    public void sendCommand(String command, String arguments) throws IOException{
        toServer.write(command+" "+arguments+'\n');
        toServer.flush();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket clientSock = new Socket ("localhost", 12345);
        Client_Main userUI = new Client_Main(clientSock);

        boolean exitflag = false;

        System.out.println("====== User Client ======\nWelcome\n");

        while(!exitflag){
            if(userUI.login() == -1)
                exitflag = true;
            while(userUI.isLogged())
                   userUI.userMenu();

                }
        }
    }


