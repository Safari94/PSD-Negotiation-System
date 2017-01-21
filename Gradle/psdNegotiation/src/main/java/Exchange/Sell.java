package Exchange;

import co.paralleluniverse.actors.ActorRef;

/**
 * Created by xavier on 29/12/16.
 */
public class Sell {

    public String company;
    public int amount;
    public float price;
    public String username;


    public Sell(String company,int amount,float price,String username){

        this.company=company;
        this.amount=amount;
        this.price=price;
        this.username=username;

    }

}
