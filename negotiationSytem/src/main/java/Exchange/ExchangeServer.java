/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;



import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;

class MsgLoginOK{
    
    
    final ActorRef serve;
    final User u;
    
    MsgLoginOK(User u,ActorRef rf){
    this.serve=rf;
    this.u=u;
    }        

    


}

class MsgLoginFailed{
    
    final User u;
    public MsgLoginFailed() {
        this.u = null;
    }
    
    

    
}

public class ExchangeServer {
    
     
    
    

    
}
