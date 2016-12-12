/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import co.paralleluniverse.actors.ActorRef;

/**
 *
 * @author toanjo
 */
public class Auth {
    public final String user;
    public final String pass;
    public final String accountnumber;
    public final ActorRef ref;
    
    public Auth(String user, String pass, String accountnumber,ActorRef ref){
        this.user=user;
        this.pass=pass;
        this.accountnumber=accountnumber;
        this.ref=ref;
    }
}
