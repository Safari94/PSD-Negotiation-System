package Client;

import static Client.Client_Main.creatBuy;
import static Client.Client_Main.creatSell;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import com.google.protobuf.Message;
import java.io.IOException;
import java.net.InetSocketAddress;
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
    ActorRef cli;
    MsgB(Buy b,ActorRef cli) { this.b= b; this.cli=cli;}
}


class MsgS {
    final Sell s;
    ActorRef cli;
    MsgS(Sell s,ActorRef cli) { this.s= s; this.cli=cli; }
}
    


public class Client_Actor extends Actor<Message, Void> {
    
    User u;
    ActorRef cli;
    FiberSocketChannel fc;
 
    
    public Client_Actor(User u,int port) throws IOException{
        this.u=u;
        fc.bind(new InetSocketAddress(port));
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
                    cli.send(new MsgS(sl,self()));
                    
                    
                case 2:
                    
                    System.out.println("Name of the company:");
                    company=sc.nextLine();
                    System.out.println("Number of shares to Buy:");
                    ns = sc.nextInt();
                    System.out.println("Price");
                    price = sc.nextFloat();
                    Buy by= creatBuy(company, ns, price);
                    cli.send(new MsgB(by,self()));
                    
                    
                case 3:
                    
                    break;
                    
            }
        }
       
        
    }
        
}
    
    
    
    
    

   
    
   
    
    
  
