/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.*;
import java.net.*;

/**
 *
 * @author Xavier Rodrigues , Antonio Anjo, João Monteiro
 */
public class Client {
    
    public static void main(String[] args){
       
    try{
        if(args.length<2)
        System.exit(1);
        String host = args[0];
        int port = Integer.parseInt(args[1]);
         Socket s = new Socket(host, port);
        CodedInputStream cis = CodedInputStream.newInstance(s.getInputStream());
        CodedOutputStream cos = CodedOutputStream.newInstance(s.getOutputStream());
        Person p = createPerson();
        AddressBook a = AddressBook.newBuilder().addPerson(p).build();
        byte[] ba = a.toByteArray();
        while (true) {
             System.out.println("Len: " + ba.length);
             cos.writeRawVarint32(ba.length);
             System.out.println("Wrote Len");
             cos.writeRawBytes(ba);
             System.out.println("Wrote " + ba.length + " bytes");
             cos.flush();
             Thread.sleep(3000);
           }
    
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
    }
  }

  static Buy createBuy(String company, int actions, float price) {
    return
      Buy.newBuilder()
      .setCompany(company)
      .setActions(actions)
      .setPrice(price)
     .build();
  }
  
  static Sell creatSell() {
    return
      Bue.newBuilder()
      .setId(1234)
      .setName("John Doe")
      .setEmail("jdoe@example.com")
      .addPhone(
        Person.PhoneNumber.newBuilder()
          .setNumber("555-4321")
          .setType(Person.PhoneType.HOME))
      .build();

}
    
    

