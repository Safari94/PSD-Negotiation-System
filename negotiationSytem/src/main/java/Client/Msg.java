package Client;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;

public class Msg {

   final Type type;
   final Object o;

   Msg(Type type, Object o) { this.type = type; this.o = o; }
 }