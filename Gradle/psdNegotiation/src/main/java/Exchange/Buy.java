package Exchange;

import co.paralleluniverse.actors.ActorRef;

/**
 * Created by xavier on 29/12/16.
 */
public class Buy {

    private ActorRef actor;
    public String company;
    public int amount;
    public float price;
    public String username;


    public Buy(String company,int amount,float price,String username){

        this.company=company;
        this.amount=amount;
        this.price=price;
        this.username=username;
        this.actor = null;

    }
    
    public Buy(String company,int amount,float price,String username, ActorRef actor){
        
        this.company=company;
        this.amount=amount;
        this.price=price;
        this.username=username;
        this.actor = actor;
        
    }

    public String getCompany(){
        return this.company;
    }
    
    public String getUser(){
        return this.username;
    }
    
    public int getAmount(){
        return this.amount;
    }
    
    public float getPrice(){
        return this.price;
    }
    
    public ActorRef getActor(){
        return this.actor;
    }
    
    public void setActor(ActorRef actor){
        this.actor = actor;
    }

}
