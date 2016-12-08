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
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class MsgLoginOK{    
    
    final ActorRef serve;
    final User u;
    
    public MsgLoginOK(User u,ActorRef rf){
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

/*class Request {
        final ActorRef from;
        final Exchange.Buy buy;
        final Exchange.Sell sell;
        Request(ActorRef from, Exchange.Sell sell){
            this.from=from;
            this.buy=null;
            this.sell=sell;
        }
        Request(ActorRef from, Exchange.Buy buy){
            this.from=from;
            this.buy=buy;
            this.sell=null;
        }
    }
*/

public class ExchServer extends Actor<Message,Void> {
    
    @Override
    public Void doRun() {        
        try{
            ServerSocket ss = new ServerSocket(6063);
            System.out.println("Exchange Server started.");
        
            while(true){
                Socket client = ss.accept();
                new Thread(new LoginHandler(client)).start();
            }
        
        } catch (Exception e){}
        return null;
    }
     
    static class LoginHandler extends Thread{
        
        Socket c;
        
        public LoginHandler(Socket c){
            this.c=c;
        }
        
        @Override
        public void run(){
            try{
                CodedInputStream cis = CodedInputStream.newInstance
                                        (c.getInputStream());
                CodedOutputStream cos = CodedOutputStream.newInstance
                                        (c.getOutputStream());
            
                //Verificação de login
                
            
            } catch (IOException e){}
        }
    }        
        
}
  
