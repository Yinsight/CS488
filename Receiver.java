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
			byte[] data = new byte[1028]; //size of seq + file data
			byte[] fileData = new byte[1024]; //size of file data
			DatagramSocket datagramSocketL = new DatagramSocket(ownPort);    //to receive datagrams
			DatagramSocket datagramSocketS = new DatagramSocket();				 // to send ack
			DatagramSocket datagramSocketR = new DatagramSocket();
			datagramSocketL.setSoTimeout(30000);
			System.out.println("Receiver: Listening");
			
		    int expected=1;
			
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
					System.out.println("Sequence number to be generated");
					seqNum= ByteBuffer.wrap(copyOfRange(data, 0, 4)).getInt();
					System.out.println("Receiver: Received sequence number: " + seqNum);
					
					if(expected==seqNum) {
						System.out.println("Yes");
						buffer.enqueue(receivePacket);		// make changes to circular queue
					}
					else {
						
						DatagramPacket dummyPacket = new DatagramPacket(intToBytes(-1),intToBytes(-1).length);
						buffer.enqueue(dummyPacket);
					}
					expected++;
					
					//add to buffer (Circular Queue)
					
										
				}
				catch (SocketTimeoutException e) {
					break;
				}
				
				
			
			}	
			
					

				for(int i=0;i<buffer.maxSize;i++) {		
				//check if packet was received and send ack
				byte[] check = new byte[1028];
				DatagramPacket checkPacket = new DatagramPacket(check,check.length);
				datagramSocketR.receive(buffer.peek(i));
				
				
						
				int v = bytesToInt(copyOfRange(check, 0, 4));
				System.out.println(v);

					// Send ack for all datagrams that are valid
					if(v!=-1) {
					// Recover seqNum from datagram and send ack
					byte[] ackNumBytes = copyOfRange(check, 0, 4);
					DatagramPacket ackPacket = new DatagramPacket(ackNumBytes, ackNumBytes.length, host, targetPort);
					datagramSocketS.send(ackPacket);
					System.out.println("Receiver: Sent Acknowledgement" + ByteBuffer.wrap(copyOfRange(check, 0, 4)).getInt());
					
					}
					
					}
					
				byte[] check = new byte[1028];
				DatagramPacket checkPacket = new DatagramPacket(check,check.length);
				checkPacket=buffer.peekHead();
				int v = bytesToInt(check);
				
				while (!(buffer.isEmpty()) && v!=-1) {
						byte[] checkn = new byte[1028];
						DatagramPacket checkPacketn = new DatagramPacket(check,check.length);
						checkPacket=buffer.peekHead();
						v = bytesToInt(checkn);
						buffer.dequeue();
						fis.write(checkn);

				}
					
			
			
			
		datagramSocketL.close(); 
		datagramSocketS.close();
		fis.close();
		
	}
	
	public static byte[] intToBytes( final int i ) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		return bb.array();
	}
	
	public static int bytesToInt( byte[] v) {
		return ByteBuffer.wrap(v).getInt();
	}
	
	public static byte[] copyOfRange(byte[] srcArr, int start, int end){
		int length = (end > srcArr.length)? srcArr.length-start: end-start;
		byte[] seqArr = new byte[length];
		System.arraycopy(srcArr, start, seqArr, 0, length);
		return seqArr;
	}
}
