package Exchange;

/**
 * Created by xavier on 29/12/16.
 */
public class Pedidos {

    public String usr1;
    public String usr2;
    public String company;
    public int amount;
    public float price;

    public Pedidos(String usr1, String usr2, String company, int amount, float price) {
        this.usr1 = usr1;
        this.usr2 = usr2;
        this.company = company;
        this.amount = amount;
        this.price = price;
    }

    public String toString(){
        return new String(usr1+" "+usr2+" "+company+" "+amount+" "+price);
    }
}
