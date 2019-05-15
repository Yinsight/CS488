import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

//CommandLine reference:
//https://commons.apache.org/proper/commons-cli/javadocs/api-1.3.1/org/apache/commons/cli/CommandLine.html

public class pinger {

	static long currentTime; // a counter that runs when program connects
	static long maxTime = currentTime + 20; // used to check while loop
	static byte[] data = new byte[1000]; // data to be sent
	static long accumulator = 0; // keep track of how much data is sent
	static long accKB = 0;
	static long accMB = 0;
	static long elapsed_Time = 0; // starts once entered the while loop
	static long throughput; // accumulator / elapsed-Time

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
		cmd.saveFlagValue("-t"); // time

		if (!cmd.hasFlag("-h") || !cmd.hasFlag("-p") || !cmd.hasFlag("-t") || cmd.numberOfFlags() > 4) {
			throw new IllegalArgumentException("Error: missing or additional arguments");
		}

		int port = getInt(cmd.getFlagValue("-p"));

		if (port < 1024 || port > 65535) {
			throw new IllegalArgumentException("Error: port number must be in the range of 1024 to 65535");
		}
		return cmd;
	}

	public static CommandLine checkargsforServer(String[] args) {

		CommandLine cmd = new CommandLine();
		cmd.saveFlagValue("-p"); // port
		cmd.parse(args);

		if (!cmd.hasFlag("-p") || cmd.numberOfFlags() > 2) {
			throw new IllegalArgumentException("Error: missing or additional arguments");
		}

		int port = getInt(cmd.getFlagValue("-p"));

		if (port < 1024 || port > 65535) {
			throw new IllegalArgumentException("Error: port number must be in the range of 1024 to 65535");
		}
		return cmd;
	}

	public static void main(String[] args) throws IOException {

		CommandLine cmd;

		if (checkargs(args) == false) {

			cmd = checkargsforClient(args);
			String host = cmd.getFlagValue("-h");
			int timeinsec = getInt(cmd.getFlagValue("-t"));
			int port = getInt(cmd.getFlagValue("-p"));
			

		} else {
			checkargsforServer(args);
			cmd = checkargsforServer(args);
			int port = getInt(cmd.getFlagValue("-p"));
			
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
					if (flagsWithValues.contains(args[n]) && n < args.length - 1) {
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


}
