import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class iperf {
	
	int currentTime;		// a counter that runs when program connects
	int maxTime=currentTime+20; // used to check while loop
	byte[] byteArray = new byte[1000]; // 
	byte accumulator;			// keep track of how much data is sent 
	int elapsed_Time;			// starts once entered the while loop
	int throughput;				// accumulator / elapsed-Time
	private Socket socket = null;
	
	public iperf(String ip, int port){
		try {
			socket = new Socket(ip, port);				// IP and port 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int throughPut(){
		
		
		while(elapsed_Time<maxTime){
			
		}
		
	}
	
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter IP");
		String ip = sc.next();
		System.out.println("Enter port");
		int port = sc.nextInt();
		
		iperf iperf = new iperf(ip,port);
	}

} 
