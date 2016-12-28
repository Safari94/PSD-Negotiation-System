/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import Client.*;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import java.util.*;

/**
 *
 * @author xavier
 */

    
public class SettlementHandler extends BasicActor<Msg,Void>{
    
    final ArrayList<Buy> buys;
    final ArrayList<Sell> sells;
    final LinkedList<Pedido> pedidos;
    
    
    SettlementHandler(){
		this.buys= new ArrayList<>();
                this.sells= new ArrayList<>();
                this.pedidos= new LinkedList<>();
		
	}
    
    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
	
        
        while(receive(msg -> { 

            switch(msg.type){

		case BUY:
                    Buy b1= (Buy)msg.o;	
                    for(Sell s: this.sells){
                        if(s.company.equals(b1.company) && s.price> b1.price){
                            float p=(s.price+b1.price)/2;
                            String c = s.company;
                            String u1=s.usr;
                            String u2=b1.usr;
                            if(s.ammount>b1.ammount){
                                buys.add(new Buy(s.usr,s.company,(s.ammount-b1.ammount),s.price));
                                pedidos.add(new Pedido(u1,u1,c,b1.ammount,p));
                                sells.remove(s);
                            }
                             
                            if(s.ammount<b1.ammount){
                                sells.add(new Sell(s.usr,s.company,(s.ammount-b1.ammount),s.price));
                                pedidos.add(new Pedido(u1,u1,c,b1.ammount,p));
                                sells.remove(s);
                            }
                             
                            if(s.ammount==b1.ammount){
                                pedidos.add(new Pedido(u1,u1,c,b1.ammount,p));
                                sells.remove(s);
                            }
                        }
                    }
                    buys.add(b1);
                                        
                    
                    return true;

				
		case SELL:
                    
                    Sell s1= (Sell)msg.o;
                    for(Buy b: this.buys){
                        if(b.company.equals(s1.company) && b.price> s1.price){
                            
                            float p=(b.price+s1.price)/2;
                            String c = b.company;
                            String u1=b.usr;
                            String u2=s1.usr;
                            if(b.ammount>s1.ammount){
                               buys.add(new Buy(b.usr,b.company,(b.ammount-s1.ammount),b.price));
                               pedidos.add(new Pedido(u1,u1,c,s1.ammount,p));
                               buys.remove(b);
                            }
                             
                            if(b.ammount<s1.ammount){
                                sells.add(new Sell(b.usr,b.company,(b.ammount-s1.ammount),b.price));
                                pedidos.add(new Pedido(u1,u1,c,s1.ammount,p));
                                buys.remove(b);
                            }
                              
                            if(b.ammount==s1.ammount){ 
                                pedidos.add(new Pedido(u1,u1,c,s1.ammount,p));
                                buys.remove(b); 
                            }
                        }
                    
                    }
                    sells.add(s1);
                   
                    return true;
            }

            return false;
	}));
            return null;
	}
    
    
}
    


