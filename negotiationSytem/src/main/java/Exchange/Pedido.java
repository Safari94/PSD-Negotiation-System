/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

/**
 *
 * @author xavier
 */
public class Pedido {
    
    public String usr1;
    public String usr2;
    public String company;
    public int amount;
    public float price;

    public Pedido(String usr1, String usr2, String company, int amount, float price) {
        this.usr1 = usr1;
        this.usr2 = usr2;
        this.company = company;
        this.amount = amount;
        this.price = price;
    }
    
    
    
}
