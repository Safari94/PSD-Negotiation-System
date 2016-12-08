package Exchange;

import proto_client.Client.User;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;
import java.util.*;

/**
 *
 * @author xavier
 */
public class Sell{    
    User u;
    String company;
    int actions;
    float price;

    public Sell(){
        
    }
}
