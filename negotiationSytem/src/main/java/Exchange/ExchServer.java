package Exchange;

import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;
import java.io.IOException;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import java.net.ServerSocket;

import java.util.ArrayList;




public class ExchServer {
    
<<<<<<< HEAD
    int port=12345;
    int settlementPort=12456;
    
    ActorRef login = new checkLogin().spawn();
    
        
=======
    private final ArrayList<Buy> buyOrders;
    private final ArrayList<Sell> sellOrders;
    
    public ExchServer(){
        this.buyOrders=new ArrayList<>();
        this.sellOrders=new ArrayList<>();
    }
    
    public static void main (String[] args){
        
        int port=6063;
        Acceptor acceptor = new Acceptor(port);
        acceptor.spawn();
    }
     
    /*                
        @Override
                
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
<<<<<<< HEAD
                if(!found)cos.write(null);*/        
=======
                if(!found)cos.write(null);
            
            } catch (IOException e){}
        }
    }*/        
>>>>>>> origin/master
>>>>>>> ee30a09b8f865148fd341e39a26d17aaa0e65bc4
        
}
  
