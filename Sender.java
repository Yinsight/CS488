package lab1.cs488.pace.edu;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {
	//int datagramBuffer[] = new int[];
	final static int ownPort = 7777;
	final static int targetPort = 8888;
	static InetAddress host = null;
	static {
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 /*
	 protected RDatagramPacket extends DatagramPacket{ RDatagramPacket
	 rdatagramPacket = new RDatagramPacket(seqNumber); }
	 
	 protected RDatagramSocket extends DatagramSocket{ RDatagramSocket
	 rdatagramSocket = new RDatagramSocket(); }
	 */
	 

	public static void main(String[] args) throws IOException, InterruptedException {
		int seqNumber;
		File file = new File("./Resource/1.jpg");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[1024];
		byte[] seqNumberArray = new byte[Integer.SIZE / 8]; //4 seqbyte
		byte[] datawithSeq = new byte[1028];
		// Socket socket = new Socket(host.getHostAddress(), targetPort);
		DatagramSocket datagramSocket = new DatagramSocket(ownPort);
		System.out.println("Sender: connection built, about to transfer.");
		
		Integer index = 0;		
		while (fis.read(data) != -1) {
			// socket.getOutputStream().write(data);
			index ++;
			//seqNumberArray.add(index.toByteArray());
			//datawithSeq[] = 
			//seqNumberArray[] = convert int to byte array
			DatagramPacket packet = new DatagramPacket(data, data.length, host, targetPort);
			
			//CircularQueue buffer = new CircularQueue[size];
			//buffer.enqueue();
			
			// Do the following for Project 1:
			// implement ReliableDatagramPacket extends DatagramPacket
			// [seq number|packet]
			// seq ++;
			
			datagramSocket.send(packet);
		}
		// add a loop to send ACKs
		System.out.println("Sender: finished.");
		datagramSocket.close();
		fis.close();
	}
	
	//array of acknowledgement
	

}
