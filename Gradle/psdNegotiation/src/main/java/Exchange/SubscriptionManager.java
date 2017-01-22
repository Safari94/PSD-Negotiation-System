package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import java.util.HashMap;


public class SubscriptionManager extends BasicActor<Message, Void> {

    private final HashMap<String,ActorRef> subscriptionList;
    private final ActorRef publisher;
    
    public SubscriptionManager(ActorRef publisher){
        this.subscriptionList = new HashMap<>();
        this.publisher = publisher;
    }
    
    
    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {
            while(receive(message -> {
                
                ActorRef user;
                String infoEnterprise;
                //String infoPrice;
                //String infoAmount;
                //String infoAll;
                switch(message.type){
                    case BUY:
                        Buy buyrqst = (Buy) message.o;
                        infoEnterprise = "info:" + buyrqst.getCompany() + " Quantity : " + buyrqst.getAmount() + " for a price of : " + buyrqst.getPrice() + " buy";

                        //infoEnterprise = "info:" + buyrqst.getCompany() + " buy";
                        //infoAll = "info : Enterprise " + buyrqst.getCompany() + " Quantity : " + buyrqst.getAmount() + " for a price of : " + buyrqst.getPrice() + " buy";


                        publisher.send(new Message(null,infoEnterprise));
                        //publisher.send(new Message(null,infoAmount));
                        //publisher.send(new Message(null,infoPrice));
                        //publisher.send(new Message(null,infoAll));
                        
                        return true;
                        
                    case SELL:
                        Sell sellrqst = (Sell) message.o;
                        infoEnterprise = "info:" + sellrqst.getCompany() + " Quantity : " + sellrqst.getAmount() + " for a price of : " + sellrqst.getPrice() + " sell";
                        //infoEnterprise = "info:Enterprise : Enterprise " + sellrqst.getCompany() + " sell";
                        //infoAmount = "info:Amount : Amount " + sellrqst.getAmount() + " sell";
                        //infoPrice = "info:Price : Price " + sellrqst.getPrice() + " sell";
                        //infoAll = "info : Enterprise " + sellrqst.getCompany() + " Quantity : " + sellrqst.getAmount() + " for a price of : " + sellrqst.getPrice() + " sell";

                        publisher.send(new Message(null,infoEnterprise));
                        //publisher.send(new Message(null,infoAmount));
                        //publisher.send(new Message(null,infoPrice));
                        //publisher.send(new Message(null,infoAll));
                        
                        return true;
                }
            
                return null;
            }));
            
            
            return null;
    }
}
