/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author xavier
 */
public class Sell {
    
    public final String usr;
    public final String company;
    public final int ammount;
    public final float price;

    public Sell(String usr, String company, int ammount, float price) {
        this.usr = usr;
        this.company = company;
        this.ammount = ammount;
        this.price = price;
    }
    
    
    
}
