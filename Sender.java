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
import java.util.HashSet;
import java.util.ArrayList;

public class Sender {
	// int datagramBuffer[] = new int[];
	final static int ownPort = 7777;
	final static int targetPort = 8888;
	static InetAddress host = null;
	static HashSet<Integer> hashTable = new HashSet<>();
	static ArrayList<DatagramPacket> packets = new ArrayList<>();
	static DatagramSocket datagramSocket = null;
	static {
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// define isAcked function
	public static boolean isAcked(byte[] data) {
		int seq = bytesToInt(copyOfRange(data, 0, 4));
		if (hashTable.contains(seq)) {
			hashTable.remove(seq);
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		CircularQueue buffer = new CircularQueue(3);
		// int seqNumber;
		File file = new File("./Resource/1.jpg");
		RandomAccessFile fis = new RandomAccessFile(file, "r");
		byte[] data = new byte[1024];

		int index = 1;
		datagramSocket = new DatagramSocket();
		System.out.println("Sender: connection built, about to transfer.");

		int ack = 0;
		
		do{
		while (fis.read(data) != -1) {
			while (!buffer.isFull()) {
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArray); 													// array
				byte[] finalData = byteArray.toByteArray();
				DatagramPacket packet = new DatagramPacket(byteArray.toByteArray(), finalData.length, host, targetPort);
				buffer.enqueue(packet);
				packets.add(packet);
				sendPacket(packet);
				index++;
				System.out.println("Sent packet: " + index);
			}

			while (true) {
				datagramSocket.setSoTimeout(30000);
				try {
					byte[] response = new byte[4];
					DatagramPacket resPacket = new DatagramPacket(response, response.length, host, targetPort);
					datagramSocket.receive(resPacket); // receiving ACK packet
					ack = bytesToInt(response);
					hashTable.add(ack);
					System.out.println("Received ack: " + ack);
				} catch (SocketTimeoutException e) {
					break;
				}
				;
			}

			int i = 0;
			while (i < buffer.maxSize && isAcked((byte[]) buffer.peek(i))) {
				buffer.dequeue();
				i++;
			}

		}

	}
	while (file != null && !hashTable.isEmpty());
}

	public static boolean allPacketsInHashTable(CircularQueue<?> buffer, HashSet<Integer> hashTable) {
		for (DatagramPacket packet : packets) {
			if (hashTable.contains(packets)) {
				buffer.dequeue(); // ack received and verified
			} else {
				return false;
			}
		}
		return true;
	}

	public static void sendPacket(DatagramPacket packet) throws IOException {
		// datagramSocket.setSoTimeout(30000);
		datagramSocket.send(packet);
	}

	public static byte[] intToBytes(final int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		return bb.array();
	}

	public static int bytesToInt(byte[] v) {
		return ByteBuffer.wrap(v).getInt();
	}

	public static byte[] copyOfRange(byte[] srcArr, int start, int end) {
		int length = (end > srcArr.length) ? srcArr.length - start : end - start;
		byte[] seqArr = new byte[length];
		System.arraycopy(srcArr, start, seqArr, 0, length);
		return seqArr;
	}
}
