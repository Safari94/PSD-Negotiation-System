/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import co.paralleluniverse.actors.*;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberSocketChannel;

/**
 *
 * @author xavier
 */
public class SettlementHandler extends BasicActor{
    
    
    
    ActorRef sett;
    public SettlementHandler(FiberSocketChannel socket,ActorRef sett) {
        
        this.sett=sett;
    }

    @Override
    protected Object doRun() throws InterruptedException, SuspendExecution {
        return null;
    }
    
}
