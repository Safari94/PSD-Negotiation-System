package Exchange;

import co.paralleluniverse.actors.ActorRef;

/**
 * Created by xavier on 29/12/16.
 */
public class Buy {

    public String company;
    public int amount;
    public float price;
    public String username;


    public Buy(String company,int amount,float price,String username){

        this.company=company;
        this.amount=amount;
        this.price=price;
        this.username=username;

    }


}
