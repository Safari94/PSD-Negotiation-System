package Exchange;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import static co.paralleluniverse.actors.LocalActor.self;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;


public class ExchServer {
    


    
 
    public static void main (String[] args){
        
            int port_cliente = 12345;
	    int port_bank= 12346;
            
	    ActorRef userHandler = new UserHandler().spawn();
            ActorRef settlementHandler = new SettlementHandler().spawn();


	    AcceptorUser acceptorUser = new AcceptorUser(port_cliente,userHandler,settlementHandler);
	    AcceptorSettlement acceptorS = new AcceptorSettlement(port_bank,settlementHandler);

            try{
            
	    acceptorUser.spawn();
            acceptorS.spawn();	    
	    acceptorS.join();
            acceptorUser.join();
            } catch (Exception e){}
    }
}
           
  
