/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import Client.*;
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
    
    final HashMap<String,Usr> users;
    
    
    UserHandler(){
		this.users= new HashMap<String,Usr>();
		
	}
    
    protected Void doRun() throws InterruptedException, SuspendExecution {
	
        this.populate();
        while(receive(msg -> { 

            switch(msg.type){

		case LOGIN:
                    Usr u1= (Usr)msg.o;					
                    if(users.containsKey(u1.username) && users.get(u1.username).equals(u1.password)){											
			u1.rf.send(new Msg(Type.LINE,"Login with sucess\n\n".getBytes()));
                        u1.rf.send(new Msg(Type.LOGIN_OK,null)); 			
                    }
                    else {
                    u1.rf.send(new Msg(Type.LINE,"Your password or user are wrong\n\n".getBytes()));
                    u1.rf.send(new Msg(Type.LOGIN_FAILED,null));
                    }
                    return true;

				
		case LOGOUT:
                    u1= (Usr)msg.o;
                    u1.rf.send(new Msg(Type.LINE,"Comeback soon !\n\n".getBytes()));
                    u1.rf.send(new Msg(Type.LOGOUT_OK, null));
                    return true;
            }

            return false;
	}));
            return null;
	}
    
    void populate (){
        Usr u;
        u = new Usr("Xavier","xavier");
        users.put("Xavier", u);
        
        u = new Usr("Antonio","antonio");
        users.put("Antonio",u);
        
        u = new Usr("Joao","joao");
        users.put("Joao",u);
        
        u = new Usr("Jose","jose");
        users.put("Jose",u);        
    }
}
    

