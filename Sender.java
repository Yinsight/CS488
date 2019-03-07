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
		ArrayList<DatagramPacket> packets = new ArrayList<>();
		int WINDOW_SIZE = 3;

		int index = 0;
		datagramSocket = new DatagramSocket();
		System.out.println("Sender: connection built, about to transfer.");

		int ack = 0;
		while (fis.read(data) != -1) {
			if (window.size() < WINDOW_SIZE) {
				index++; // track packet being sent'
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArray); // used
																		// to
																		// put
																		// data
																		// into
																		// a
																		// byte
																		// array
				out.writeInt(index);
				out.write(data);
				byte[] finalData = byteArray.toByteArray();
				DatagramPacket packet = new DatagramPacket(byteArray.toByteArray(), finalData.length, host, targetPort);
				buffer.enqueue(packet);
				packets.add(packet);
				sendPacket(packet);
				System.out.println("Sent packet: " + index);
			} else {
				// Once window is full, wait for receiver to send
				// acknowledgement for next packet
				// Check if all the packets in window have been received
				if (allPacketsInHashTable(buffer, hashTable)) {
					buffer.dequeue();
					packets.clear();
					continue;
				}

				fis.seek(index * 1024); // Keep file reader at current position

				try {
					byte[] response = new byte[4];
					DatagramPacket resPacket = new DatagramPacket(response, response.length, host, targetPort);
					datagramSocket.setSoTimeout(10000);
					datagramSocket.receive(resPacket);
					ack = bytesToInt(response);
					hashTable.add(ack);
					System.out.println("Received ack: " + ack);
				} catch (SocketTimeoutException e) {
					System.out.println("Resending packets");
					for (int i = 0; i < window.size(); i++) {
						if (!hashTable.contains(window.get(i))) {
							// Resend missing packets
							sendPacket(packets.get(i));
							System.out.println("Resent packet: " + buffer.peek(i));
						}
					}
				}

			}

			int i = 0;
			while (i < buffer.maxSize && isAcked((byte[]) buffer.peek(i))) {
				buffer.dequeue();
				i++;
			}
		} // while part of the do-while loop

		byte[] end = intToBytes(-1);
		datagramSocket.send(new DatagramPacket(end, end.length, host, targetPort));
	}

	public static boolean allPacketsInHashTable(CircularQueue buffer, HashSet<Integer> hashTable) {
		for (Integer integer : window) {
			if (!hashTable.contains(integer)) {
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
