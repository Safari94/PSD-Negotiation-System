/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import co.paralleluniverse.actors.ActorRef;

/**
 *
 * @author xavier
 */
public class Usr {
    
    final ActorRef rf;
    final String username;
    final String password;
    
    public Usr(ActorRef rf, String username, String password){
        this.rf=rf;
        this.username=username;
        this.password=password;
    }
    
    
    
    
}
