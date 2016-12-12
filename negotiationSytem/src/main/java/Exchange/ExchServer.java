package Exchange;

import proto_client.Client.Sell;
import proto_client.Client.Buy;

import java.util.ArrayList;




public class ExchServer {
    

    private final ArrayList<Buy> buyOrders;
    private final ArrayList<Sell> sellOrders;
    
    public ExchServer(){
        this.buyOrders=new ArrayList<>();
        this.sellOrders=new ArrayList<>();
    }
    
    public static void main (String[] args){
        
        int exchangeport = 12345;
        int settlementport = 123456;
        Acceptor acceptor = new Acceptor(exchangeport);
        acceptor.spawn();
    }
     
    /*                
        @Override
                
                //Verificação de login
                
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
                if(!found)cos.write(null);
            
            } catch (IOException e){}
        }*/

        
}
  
