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
                switch(message.type){
                    case BUY:
                        Buy buyrqst = (Buy) message.o;
                        infoEnterprise = "info:" + buyrqst.getCompany() + " - Quantity: " + buyrqst.getAmount() + " - Price: " + buyrqst.getPrice() + " -- Bought by: " + buyrqst.getUser();

                        publisher.send(new Message(null,infoEnterprise));
                        
                        return true;
                        
                    case SELL:
                        Sell sellrqst = (Sell) message.o;
                        infoEnterprise = "info:" + sellrqst.getCompany() + " - Quantity: " + sellrqst.getAmount() + " - Price: " + sellrqst.getPrice() + " -- Sold by: " + sellrqst.getUser();

                        publisher.send(new Message(null,infoEnterprise));

                        return true;
                }
            
                return null;
            }));
            
            
            return null;
    }
}
