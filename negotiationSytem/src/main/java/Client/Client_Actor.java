/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import static Client.Client_Main.creatBuy;
import static Client.Client_Main.creatSell;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;
import com.google.protobuf.Message;
import java.util.Scanner;
import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;
import zmq.Msg;






/**
 *
 * @author xavier
 * 
 * 
 * 
 */

class Msg {
    final Buy b;
    final Sell s;  // careful with mutable objects, such as the byte array
    Msg(Buy b, Sell s) { this.b= b; this.s = s; }
  }


public class Client_Actor extends Actor<Message, Void> {
    
    User u;
    ActorRef cli;
    
   public  Client_Actor(User u){
    this.u=u;
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
                    cli.send(new Msg(null, sl));
                    
                    
                case 2:
                    
                    System.out.println("Name of the company:");
                    company=sc.nextLine();
                    System.out.println("Number of shares to Buy:");
                    ns = sc.nextInt();
                    System.out.println("Price");
                    price = sc.nextFloat();
                    Buy by= creatBuy(company, ns, price);
                    cli.send(new Msg(by, null));
                    
                    
                case 3:
                    
                    break;
                    
            }
        }
        
        }
        
    }
    
    
    
    
    

   
    
   
    
    
  }
