/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import co.paralleluniverse.actors.ActorRef;
import static co.paralleluniverse.actors.LocalActor.self;

/**
 *
 * @author xavier
 */
public class Usr {
    
    public final ActorRef rf;
    public final String username;
    public final String password;
    
    public Usr(ActorRef rf, String username, String password){
        this.rf=rf;
        this.username=username;
        this.password=password;
    }
    
    public Usr(String username, String password){
        this.rf=self();
        this.username=username;
        this.password=password;
    }
    
    
    
    
}
