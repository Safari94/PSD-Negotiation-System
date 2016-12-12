/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;
import Exchange.Auth;
import proto_client.Client.Sell;
import proto_client.Client.Buy;
import proto_client.Client.User;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author xavier
 */
public class Client_Main {
    
    public static void main(String[] args) {
       try{
        if(args.length<2)
        System.exit(1);
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Socket s = new Socket(host,port);
        CodedInputStream cis = CodedInputStream.newInstance(s.getInputStream());
        CodedOutputStream cos = CodedOutputStream.newInstance(s.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        
    
        Scanner sc = new Scanner(System.in);
    
    
        System.out.println("************ Bolsa Online *************");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");
        System.out.println("1. Login");
        System.out.println("2. Exit");
   
        int opt = sc.nextInt();
    
        switch(opt){
        
            case 1:
                System.out.println("Insira o seu Username");
                String user= sc.nextLine();
                System.out.println("Insira a sua password");
                String psd= sc.nextLine();
                User u = creatUser(user, psd);
                byte[] ba = u.toByteArray();
                cos.writeRawBytes(ba);
                cos.flush();
               
                System.out.println("Checking in progress..."); 
                
                Auth auth=(Auth)is.readObject();
                
               
                if(auth!=null){                
                    new Client_Actor(auth,port).spawn();                
                }
                else {
                    System.out.println("User not found!!");
                }
                break;
                
            case 2: 
                System.exit(1);              
        
            default: 
                System.exit(1);
        }
    
    
    
      
        
        s.shutdownOutput();
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
         
    }
    
    static Buy creatBuy(String company,int actions, float price){
        return 
            Buy.newBuilder()
            .setCompany(company)
            .setActions(actions)
            .setPrice(price)
            .build();
    }
    
    static Sell creatSell(String company,int actions, float price){
        return 
            Sell.newBuilder()
            .setCompany(company)
            .setActions(actions)
            .setPrice(price)
            .build();
    }
      
    static User creatUser(String user ,String pwd){
        return 
            User.newBuilder()
            .setUser(user)
            .setPass(pwd)
            .setAccountNumber("")
            .build();
    }
    
}
