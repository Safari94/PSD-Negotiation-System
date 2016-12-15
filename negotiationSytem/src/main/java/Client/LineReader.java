/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;

public class LineReader extends BasicActor<Msg, Void> {

  final ActorRef<Msg> dest;
  final FiberSocketChannel socket;
  static int MAXLEN = 1024;

  ByteBuffer in = ByteBuffer.allocate(MAXLEN);
  ByteBuffer out = ByteBuffer.allocate(MAXLEN);

  LineReader(ActorRef<Msg> dest, FiberSocketChannel socket) {
    this.dest = dest; this.socket = socket;
  }

  protected Void doRun() throws InterruptedException, SuspendExecution {
    boolean eof = false;
    byte b = 0;
    
    try {

      for(;;) { //blocking reader
        
        if (socket.read(in) <= 0) eof = true;
        in.flip();

        while(in.hasRemaining()) {
          b = in.get();
          out.put(b);
          if (b == '\n') break;
        }

        if (eof || b == '\n') { // send line
          out.flip();
          if (out.remaining() > 0) {
            byte[] ba = new byte[out.remaining()];
            out.get(ba);
            out.clear();
            dest.send(new Msg(Type.DATA, ba));
          }
        }

        if (eof && !in.hasRemaining()) break;
        in.compact();
      }

      dest.send(new Msg(Type.EOF, null));
      return null;

    } catch (IOException e) {
      dest.send(new Msg(Type.IOE, null));
      return null;
    }
  }  
}
