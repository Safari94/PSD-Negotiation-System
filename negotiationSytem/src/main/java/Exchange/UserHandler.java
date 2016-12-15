/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import Client.Msg;
import java.util.*;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

/**
 *
 * @author xavier
 */
public class UserHandler extends BasicActor<Msg, Void> {
    
    final HashMap<String,String> users;
    final ArrayList<Sell> sells;
    final ArrayList<Buy> buys;
    
    UserHandler(){
		this.users= new HashMap<String,String>();
		this.sells= new ArrayList<Sell>();
                this.buys= new ArrayList<Buy>();
	}
    
    protected Void doRun() throws InterruptedException, SuspendExecution {
		
        while(receive(msg -> { 

			switch(msg.type){

				
				case LOGIN:
					User u1= (User)msg.o;
					if(users.containsKey(data1.user) && logins.get(data1.user).equals(data1.pass)){
						if(!online.containsKey(data1.user)){
							online.put(data1.user,data1.ref);
							
							data1.ref.send(new Msg(Type.LINE,"Login with sucess\n\n".getBytes()));

							data1.ref.send(new Msg(Type.LOGIN_OK,null)); 
						}

						else{
							data1.ref.send(new Msg(Type.LINE,"This account is already online, please logout or try again later\n\n".getBytes()));
						}
					}

					else {
						data1.ref.send(new Msg(Type.LINE,"Your password or user are wrong\n\n".getBytes()));
					}
					return true;

				

				case LOGOUT:
				
                                MRoom2 dados=(MRoom2)msg.o;
				this.online.remove(dados.name);
				dados.user.send(new Msg(Type.LINE,"Comeback soon !\n\n".getBytes()));
				dados.user.send(new Msg(Type.LOGOUT_OK, null));
				return true;

				

				
			}

				return false;
		}));
			return null;
	}
}
    

    
}
