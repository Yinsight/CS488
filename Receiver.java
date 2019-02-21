package lab1.cs488.pace.edu;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Receiver {
	final static int ownPort = 8888;
	final static int targetPort = 7777;
	static InetAddress host = null;
	static {
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		File file = new File("./Resource/copy_1_udp.jpg");
		FileOutputStream fis = new FileOutputStream(file);
		byte[] data = new byte[1024];
		DatagramSocket datagramSocket = new DatagramSocket(ownPort);
		// ServerSocket socket = new ServerSocket(ownPort, 1, host);
		datagramSocket.setSoTimeout(30000);
		System.out.println("Receiver: connection built, about to receive.");
		while (true) {
			try {
				// Socket client = socket.accept();
				// do we need ip and port of the sender?
				DatagramPacket receivePacket = new DatagramPacket(data, data.length, host, targetPort);
				datagramSocket.receive(receivePacket);
				// For Project 1, maintain a buffer here.
				fis.write(data);
				// find seq number
			} catch (SocketTimeoutException e) {
				break;
			}
		}
		System.out.println("Receiver: finished.");
		datagramSocket.close();
		fis.close();
	}
}
