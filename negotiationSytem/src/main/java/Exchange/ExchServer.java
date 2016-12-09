package Exchange;

import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;
import java.io.IOException;
import co.paralleluniverse.actors.*;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    
    private final ArrayList<Buy> buyOrders;
    private final ArrayList<Sell> sellOrders;
    private final ArrayList<User> users;
    
    public ExchServer(){
        this.buyOrders=new ArrayList<>();
        this.sellOrders=new ArrayList<>();
        this.users=new ArrayList<>();
    }
    
    @Override
    public Void doRun() {      
                        
        try{
            ServerSocket ss = new ServerSocket(6063);
            System.out.println("Exchange Server started.");
        
            while(true){
                Socket client = ss.accept();
                new Thread(new LoginHandler(client,users)).start();
            }
        
        } catch (Exception e){}
        return null;
    }
     
    static class LoginHandler extends Thread{
        
        Socket c;
        ArrayList<User> users;
        
        public LoginHandler(Socket c,ArrayList<User> users){
            this.c=c;
            this.users=users;
        }
        
        @Override
        public void run(){
            try{
                CodedInputStream cis = CodedInputStream.newInstance
                                        (c.getInputStream());
                CodedOutputStream cos = CodedOutputStream.newInstance
                                        (c.getOutputStream());
                
                //Verificação de login
                
                int len = cis.readRawVarint32();
                byte[] ba = cis.readRawBytes(len);                                
                User f = User.parseFrom(ba);
                String username = f.getUser();
                String password = f.getPass();
                boolean found=false;
                
                for (User u : users){
                    if (u.getUser().equals(username) &&
                            u.getPass().equals(password)){
                        cos.writeRawBytes(u.toByteArray());
                        found=true;
                        break;                        
                    }
                }
                if(!found)cos.write(null);
            
            } catch (IOException e){}
        }
    }        
        
}
  
