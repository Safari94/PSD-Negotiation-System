/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
<<<<<<< Updated upstream
//import zmq.Msg;
=======




>>>>>>> Stashed changes

/**
 *
 * @author xavier
 * 
 * 
 * 
 */

class MsgB {
    final Buy b;
<<<<<<< Updated upstream
    final Sell s;  // careful with mutable objects, such as the byte array
    Msg(Buy b, Sell s) { this.b= b; this.s = s; }
}
=======
      // careful with mutable objects, such as the byte array
    MsgB(Buy b) { this.b= b; }
  }
>>>>>>> Stashed changes

class MsgS {
    final Sell s;
      // careful with mutable objects, such as the byte array
    MsgS(Sell s) { this.s= s; }
    
}

public class Client_Actor extends Actor<Message, Void> {
    
    User u;
    ActorRef cli;
    
<<<<<<< Updated upstream
    public  Client_Actor(User u){
        this.u=u;
=======
   
    
   public  Client_Actor(User u){
    this.u=u;
   
>>>>>>> Stashed changes
    }
    
    
    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("************ Welcome "+u.getUser()+"************");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|        1. Sell     |");
            System.out.println("|        2. Buy       |");
            System.out.println("|        3. Logout      |");
            
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
    
    
    
    
    

   
    
   
    
    
  