import java.net.*;
import java.util.*;

/*
 * Server to process ping requests over UDP.
 */
 
public class PingServer
{
   private static final int AVERAGE_DELAY = 100;  // milliseconds

   public static void main(String[] args) throws Exception
   {
      boolean runServerFlag = false;
      int port = 0;
      // Get command line argument.
      if (args.length == 0) {
         System.out.println("Required arguments: -s");
         System.out.println("Required arguments: -p <listen port> 1024 - 65536");
         return;
      }
      
      for (int i = 0; i < args.length; i++) {
         switch (args[i].charAt(0)) {
         case '-':
            if (args[i].length() < 2)
               throw new IllegalArgumentException("Not a valid argument: " + args[i]);
            if (args[i].charAt(1) == 's') {
               runServerFlag = true;
            }
            if (args[i].charAt(1) == 'p') {
               if (args.length - 1 == i)
                  throw new IllegalArgumentException("Expected arg after: " + args[i]);
               port = Integer.parseInt(args[i + 1]);
            }
            break;
         default:
            if(args[i-1].charAt(0) == '-') {
               break;
            } else {
               System.out.println("Required arguments: -s");
               System.out.println("Required arguments: -p <listen port> 1024 - 65536");
               return;
            }
         }
      }
      if(runServerFlag) {
         if(port > 1024 && port < 65536){
            runServer(port);
         } else {
            System.out.println("Port is outside of the acceptable range 1024 - 65536");
         }
      }
      
   }

   @SuppressWarnings("resource")
private static void runServer(int port) throws Exception {

      // Create random number generator for use in simulating
      // packet loss and network delay.
      Random random = new Random();

      // Create a datagram socket for receiving and sending UDP packets
      // through the port specified on the command line.
      DatagramSocket socket = new DatagramSocket(port);

      // Processing loop.
      while (true) {
         // Create a datagram packet to hold incoming UDP packet.
         DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

         // Block until the host receives a UDP packet.
         socket.receive(request);

         // Decide whether to reply, or simulate packet loss.
         if (random.nextInt(10) < 3) {
            InetAddress clientHost = request.getAddress();
            int clientPort = request.getPort();
            byte[] buffer = request.getData().toString().toUpperCase().getBytes();

            DatagramPacket reply = new DatagramPacket(buffer, buffer.length, clientHost, clientPort);
            socket.send(reply);
            continue;
         }

         // Simulate network delay.
         Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

         // Send reply.
         InetAddress clientHost = request.getAddress();
         int clientPort = request.getPort();
         byte[] buffer = request.getData();
         DatagramPacket reply = new DatagramPacket(buffer, buffer.length, clientHost, clientPort);
         socket.send(reply);
      }
   }
}
