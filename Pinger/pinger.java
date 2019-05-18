import java.util.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

//CommandLine reference:
//https://commons.apache.org/proper/commons-cli/javadocs/api-1.3.1/org/apache/commons/cli/CommandLine.html

public class Pinger {

	static long currentTime; // a counter that runs when program connects
	static long maxTime = currentTime + 20; // used to check while loop
	static byte[] data = new byte[1000]; // data to be sent
	static long accumulator = 0; // keep track of how much data is sent
	static long accKB = 0;
	static long accMB = 0;
	static long elapsed_Time = 0; // starts once entered the while loop
	static long startTime;
	
	static DatagramSocket datagramSocket = null;
	static long latency; // latency = elapsed-Time - timeout
	private static final double LOSS_RATE = 0.3;
	static int checker = 0;
	private static boolean timeout = true;

	public static boolean checkargs(String[] args) {

		CommandLine cmd = new CommandLine();
		cmd.saveFlagValue("-s"); // client state
		cmd.parse(args);
		cmd.getFlagValue("-s");

		if (cmd.hasFlag("-s")) {
			if (cmd.getFlagValue("-s") == "0") {
				return false;
			} else {
				return true;
			}
		} else {
			throw new IllegalArgumentException("Error: missing arguments");
		}
	}

	public static CommandLine checkargsforClient(String[] args) {

		CommandLine cmd = new CommandLine();
		cmd.saveFlagValue("-h"); // host
		cmd.saveFlagValue("-p"); // port
		cmd.saveFlagValue("-n"); // number of packets
		cmd.parse(args);

		if (!cmd.hasFlag("-h") || !cmd.hasFlag("-p") || !cmd.hasFlag("-n")) {
			throw new IllegalArgumentException(
					"Error: missing or additional arguments");
		}

		int port = getInt(cmd.getFlagValue("-p"));

		if (port < 1024 || port > 65535) {
			// throw new
			// IllegalArgumentException("Error: port number must be in the range of 1024 to 65535");
		}
		return cmd;
	}

	public static CommandLine checkargsforServer(String[] args) {

		CommandLine cmd = new CommandLine();
		cmd.saveFlagValue("-p"); // port
		cmd.parse(args);

		if (!cmd.hasFlag("-p") || cmd.numberOfFlags() > 2) {
			throw new IllegalArgumentException(
					"Error: missing or additional arguments");
		}

		int port = getInt(cmd.getFlagValue("-p"));

		if (port < 1024 || port > 65535) {
			throw new IllegalArgumentException(
					"Error: port number must be in the range of 1024 to 65535");
		}
		return cmd;
	}

	public static void main(String[] args) throws Exception {

		CommandLine cmd;

		if (checkargs(args) == false) {

			cmd = checkargsforClient(args);

			String host = cmd.getFlagValue("-h");
			int port = getInt(cmd.getFlagValue("-p"));
			int nofpackets = getInt(cmd.getFlagValue("-n"));
			latencyClient(host, port, nofpackets);

		} else {
			checkargsforServer(args);
			cmd = checkargsforServer(args);
			int port = getInt(cmd.getFlagValue("-p"));
			latencyServer(port);

		}
	}

	private static int getInt(String portString) {
		try {
			return Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static class CommandLine {
		private String[] arguments = null;

		private Map<String, String> flags = new HashMap<>();

		private Set<String> flagsWithValues = new HashSet<>();

		public String[] getArguments() {
			return arguments;
		}

		String getFlagValue(String flagName) {
			return flags.get(flagName);
		}

		int numberOfFlags() {
			return flags.size();
		}

		boolean hasFlag(String flagName) {
			return flags.containsKey(flagName);
		}

		void parse(String[] args) {
			List<String> regularArgs = new ArrayList<>();
			for (int n = 0; n < args.length; ++n) {
				if (args[n].charAt(0) == '-') {

					if (args[n].charAt(1) == 'c') {
						flags.put("-s", "0");
					} else {
						if (args[n].charAt(1) == 's') {
							flags.put("-s", "1");
						}
					}

					String name = args[n];
					String value = null;

					// Check for flag in flagWithValues and get the next arg as
					// the value if it exists
					if (flagsWithValues.contains(args[n])
							&& n < args.length - 1) {
						value = args[++n];
					}
					flags.put(name, value);
				} else {
					regularArgs.add(args[n]);
				}
			}

			int size = regularArgs.size();
			arguments = regularArgs.toArray(new String[size]);
		}

		// Add flags which have values
		void saveFlagValue(String flagName) {
			flagsWithValues.add(flagName);
		}
	}

	public static void latencyClient(String h, int port, int nofpackets)
			throws IOException {

		InetAddress host = null;
		host = InetAddress.getByName(h);

		DatagramSocket clientsocket = new DatagramSocket(port);

		int nofpacketlost = 0;
		int count = 0;
		int sum = 0;
		long max = 0;
		long min = 1;
		long avgRTT = 0;
		long totalTime = 0;

		long start = 0;

		for (int i = 0; i < nofpackets; i++) {
			// create string to send
			String str = "PING";
			byte[] buff = new byte[1024];
			buff = str.getBytes();
			// Create Datagram packet to send as UDP Packet
			// check if initialized correctly (port removed)
			DatagramPacket ping = new DatagramPacket(buff, buff.length, host,
					port);
			// start time (nanoseconds)
			start = System.currentTimeMillis();

			// send ping to specified server
			clientsocket.send(ping);

			try {

				DatagramPacket resPacket = new DatagramPacket(new byte[1024],
						1024);
				clientsocket.setSoTimeout(1000);
				clientsocket.receive(resPacket); // replace null with resPacket
				latency= System.currentTimeMillis() - start;
				totalTime = totalTime + latency;
				if (min > latency) {
					min = latency;
				}
				if (max < latency) {
					max = latency;
				}
				sum += latency;
				count++;

			} catch (SocketTimeoutException e) {
				System.out.println("packet " + i + " timed out.");
				nofpacketlost++;
			}

		}
		clientsocket.close();
		avgRTT = totalTime / sum;
		System.out.println("Average RTT = " + avgRTT);
		System.out.println("Min Latency = " + min);
		System.out.println("Max Latency = " + max);
		System.out.println("Average Latency = " + sum / count);
		System.out.println("No. of packets lost = " + nofpacketlost);
	}

	private static void latencyServer(int port) throws Exception {
		Random random = new Random();
		int nofpacketlost = 0;
		int count = 0;
		int sum = 0;
		long max = 0;
		long min = 0;
		int checker = 0;
		long start = 0;
		long totalTime = 0;
		long avgRTT = 0;
		// Create a datagram socket for receiving and sending UDP packets
		// through the port specified on the command line.
		DatagramSocket serversocket = new DatagramSocket(port); // Processing
																// loop.

		while (true) {
			try {

				serversocket.setSoTimeout(10000);

				// Create a datagram packet to hold incomming UDP packet.
				DatagramPacket request = new DatagramPacket(new byte[1024],
						1024);
				// Block until the host receives a UDP packet.
				start = System.currentTimeMillis();
				serversocket.receive(request);

				// decide whether to reply or simulate a packet loss
				if (random.nextDouble() < LOSS_RATE) {
					System.out.println(" Reply not sent.");
					nofpacketlost++;
					continue;
				} else {
					// Send reply.
					InetAddress clientHost = request.getAddress();
					int clientPort = request.getPort();
					byte[] buf = request.getData();
					DatagramPacket reply = new DatagramPacket(buf, buf.length,
							clientHost, clientPort);
					serversocket.send(reply);
					latency = System.currentTimeMillis() - start;
					totalTime = totalTime + latency;
					if (min > latency) {
						min = latency;
					}
					if (max < latency) {
						max = latency;
					}
					sum += latency;
					count++;
					System.out.println(" Reply sent.");
				}
			} catch (SocketTimeoutException e) {
				if (sum != 0) {
					avgRTT = totalTime / sum;
					System.out.println("Average RTT = " + avgRTT);
					System.out.println("Min Latency " + min);
					System.out.println("Max Latency " + max);
					System.out.println("Average Latency " + sum / count);
					System.out.println("No. of packets lost " + nofpacketlost);
				} else if (checker > 0) {
					System.out
							.println("Timeout. Assume Client is finished. Closing socket.");
					timeout = false;
				}

				else {
					System.out.println("No connection found. Retrying...");
				}
				break;
			}
			
		}
		serversocket.close();
	}
}
