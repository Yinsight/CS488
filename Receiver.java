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

public class Receiver {
	
	final static int targetPort = 7777;
	final static int ownPort = 8888;
	static int seqNum = 0;                 // datagram sequence number
	static InetAddress host = null;
	
	public static void main(String[] args) throws IOException {
		
		// Initializing buffer of size 3
			CircularQueue buffer= new CircularQueue(3);
		
			File file = new File("./Resource/copy_1_udp.jpg");
			FileOutputStream fis = new FileOutputStream(file);
		        byte[] data = new byte[1024];
		    
			DatagramSocket datagramSocketL = new DatagramSocket(targetPort);    //to receive datagrams
			DatagramSocket datagramSocketS = new DatagramSocket();				 // to send ack
			datagramSocketL.setSoTimeout(30000);
			System.out.println("Receiver: Listening");
			
			
			try {
				host = InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			//boolean running = true;
			
			while(!buffer.isFull()) {
				System.out.println("Enter While loop");
				try {
					DatagramPacket receivePacket = new DatagramPacket(data, data.length);	// incoming packet
					datagramSocketL.receive(receivePacket);			//get data
			
					//break down of data - get sequence number				
					//needs to match how sender manages the sequence Number
					seqNum = ByteBuffer.wrap(copyOfRange(data, data.length-5, data.length-1)).getInt();
					System.out.println("Receiver: Received sequence number: " + seqNum);
					
					
					//add to buffer (Circular Queue)
					buffer.enqueue(seqNum);
					
										
				}
				catch (SocketTimeoutException e) {
					break;
				}
			
			}	
			
			//Go through Queue to send ack for datagrams received
			for(int i=0;i<buffer.maxSize;i++) {
				int check = buffer.peekHead();			
				if(check !=0) {							// assuming that no packet has sequence number 0 and we use 0 to allocate space for datagrams not received
					byte[] ackNumBytes = ByteBuffer.allocate(4).putInt(check).array();
					DatagramPacket ackPacket = new DatagramPacket(ackNumBytes, ackNumBytes.length, host, targetPort);
					datagramSocketS.send(ackPacket);
					System.out.println("Receiver: Sent duplicate Ack " + check);
				}
			}
			
			
			
		datagramSocketL.close(); 
		datagramSocketS.close();
		fis.close();
		
	}
		
	
	public static byte[] copyOfRange(byte[] srcArr, int start, int end){
		int length = (end > srcArr.length)? srcArr.length-start: end-start;
		byte[] seqArr = new byte[length];
		System.arraycopy(srcArr, start, seqArr, 0, length);
		return seqArr;
	}
}
	
