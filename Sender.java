package lab1.cs488.pace.edu;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import lab1.cs488.pace.edu.CircularQueue;

public class Sender {
	// int datagramBuffer[] = new int[];
	final static int ownPort = 7777;
	final static int targetPort = 8888;
	static InetAddress host = null;
	static DatagramSocket datagramSocket = null;
	static boolean check = true;
	static {
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * protected RDatagramPacket extends DatagramPacket{ RDatagramPacket
	 * rdatagramPacket = new RDatagramPacket(seqNumber); }
	 * 
	 * protected RDatagramSocket extends DatagramSocket{ RDatagramSocket
	 * rdatagramSocket = new RDatagramSocket(); }
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		CircularQueue buffer = new CircularQueue(3);
		int seqNumber;
		File file = new File("./Resource/1.jpg");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[1024];
		byte[] indexinbyte = new byte[4];
		byte[] datawithSeq = new byte[1028];
		// Socket socket = new Socket(host.getHostAddress(), targetPort);
		//DatagramSocket datagramSocket = new DatagramSocket(ownPort);
		datagramSocket = new DatagramSocket;
		System.out.println("Sender: connec()tion built, about to transfer.");

		int index = 0;
		while (fis.read(data) != -1) {
			// socket.getOutputStream().write(data);
			index++;
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
          	        DataOutputStream out = new DataOutputStream(byteArray); // used to put data into a byte array
                        out.writeInt(index);
                        out.write(data);
                        byte[] finalData = byteArray.toByteArray();
			/*indexinbyte[0] = (byte) (index >>> 24);
			indexinbyte[1] = (byte) (index >>> 16);
			indexinbyte[2] = (byte) (index >>> 8);
			indexinbyte[3] = (byte) (index);
			
			// byte[] datawithSeq = new byte[1028];
			System.arraycopy(data, 0, datawithSeq, 0, 1024);
			System.arraycopy(indexinbyte, 0, datawithSeq, 1024, 4);
			*/
			DatagramPacket packet = new DatagramPacket(data, data.length, host, targetPort);
			
			int resSeq;
			
			while(check = true) {
        	        for(int i=0; i<3; i++) { // window size
                        resSeq = sendPacket(packet); // send packet
                        System.out.println("Sent packet: " + index);
                
		      /* datagramSocket.setSoTimeout(10000);		//tries to receive ACK packet
			DatagramPacket ackPacket = new DatagramPacket(ackNumBytes, ackNumBytes.length);
			try { 
				datagramSocket.receive(ackPacket);
				ackNum = receive(ackPacket);
				while (ackNum != index){	// if acknowledgment packet received is different from the current packet being tracked, send again

				System.out.println("Resending packet.");
			    }

		       } catch (SocketTimeoutException e) {
			  sendPacket(packet); 
		       }
		       */			       
                
             	         byte[] end = intToBytes(-1); //update array
                         datagramSocket.send(new DatagramPacket(end, end.length, host, targetPort));
                
            	         if(i == 3) {	
                         System.out.println("ACKs verfied. Window sliding.");
            	       	 i=0;
             }
            		}	
            	}					
            }
			System.out.println("Sender: finished.");
			datagramSocket.close();
			fis.close();
	}
			public static int sendPacket(DatagramPacket packet) throws IOException {
        		byte[] response = new byte[4];
			datagramSocket.setSoTimeout(30000);
			// CircularQueue buffer = new CircularQueue[size];
			// buffer.enqueue();

			// Do the following for Project 1:
			// implement ReliableDatagramPacket extends DatagramPacket
			// [seq number|packet]
			// seq ++;

			datagramSocket.send(packet);
			DatagramPacket resPacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
        		datagramSocket.receive(resPacket);
        		return bytesToInt(response); // return acknowledgment
		}

		

			public static byte[] intToBytes( final int i ) {
			ByteBuffer bb = ByteBuffer.allocate(3);
			bb.putInt(i);
			return bb.array();
		    }

			public static int bytesToInt(final byte[] b) {
			ByteBuffer bb = ByteBuffer.wrap(b);
			return bb.getInt();
    }

}
