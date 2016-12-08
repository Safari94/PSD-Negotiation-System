package Client;

import static Client.Client_Main.creatBuy;
import static Client.Client_Main.creatSell;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import com.google.protobuf.Message;
import java.util.Scanner;
import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;

//import zmq.Msg;


/**
 *
 * @author xavier
 */

class MsgB {
    final Buy b;
    MsgB(Buy b) { this.b= b; }
}


class MsgS {
    final Sell s;
    MsgS(Sell s) { this.s= s; }
}
    


public class Client_Actor extends Actor<Message, Void> {
    
    User u;
    ActorRef cli;
    
    
    public Client_Actor(User u){
        this.u=u;
        this.cli=Actor.currentActor().ref();
    }
    
    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("************ Welcome "+u.getUser()+"************");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("1. Sell");
            System.out.println("2. Buy");
            System.out.println("3. Logout");
            
            int opt = sc.nextInt();
            
            switch(opt){
                
                case 1: //Sell
                    System.out.println("Name of the company:");
                    String company=sc.nextLine();
                    System.out.println("Number of shares to sell:");
                    int ns = sc.nextInt();
                    System.out.println("Price");
                    float price = sc.nextFloat();
                    Sell sl=creatSell(company, ns, price);
                    cli.send(new MsgS(sl));
                    
                    
                case 2:
                    
                    System.out.println("Name of the company:");
                    company=sc.nextLine();
                    System.out.println("Number of shares to Buy:");
                    ns = sc.nextInt();
                    System.out.println("Price");
                    price = sc.nextFloat();
                    Buy by= creatBuy(company, ns, price);
                    cli.send(new MsgB(by));
                    
                    
                case 3:
                    
                    break;
                    
            }
        }
       
        
    }
        
}
    
    
    
    
    

   
    
   
    
    
  
