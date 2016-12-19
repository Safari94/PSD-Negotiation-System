package Client;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;

public class Msg {

   public final Type type;
   public final Object o;

   public Msg(Type type, Object o) { this.type = type; this.o = o; }
 }