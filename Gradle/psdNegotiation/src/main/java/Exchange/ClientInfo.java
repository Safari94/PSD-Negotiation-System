package Exchange;

/**
 * Created by xavier on 29/12/16.
 */

import co.paralleluniverse.actors.ActorRef;

public class ClientInfo {


    private String username;
    private String password;
    private ActorRef actor;


    /*
    Constructor for LoginManager HashMap. Used only for storage of information about the user.
    Doesn't require an ActorRef. Used on user creation.
    No User starts out as Administrator.
    */
    public ClientInfo(String username, String passwd){

        this.username = username;
        this.password = passwd;
        this.actor = null;
    }

    /*
    Default constructor for normal users. Must have actorref for LoginManager replies.
    */
    public ClientInfo(String username, String passwd, ActorRef actorref){

        this.username = username;
        this.password = passwd;
        this.actor = actorref;
    }



    /*
     Clone Constructor
    */
    public ClientInfo(ClientInfo existinguser){

        this.username = existinguser.username;
        this.password = existinguser.password;
        this.actor = existinguser.actor;
    }

    public String getUsername(){
        return this.username;
    }



    public void setPassword(String newpasswd){
        this.password = newpasswd;
    }

    public String getPassword(){
        return this.password;
    }

    public ActorRef getActor(){
        return this.actor;
    }

    public boolean authenticate(String password){
        return (this.password.equals(password));
    }


}
