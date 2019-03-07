package lab1.cs488.pace.edu;


import java.net.DatagramPacket;


public class CircularQueue {

	int maxSize;
	int head = -1;
	int tail = -1;
	DatagramPacket ringBuffer[];
	// need to change abstract type later

	public CircularQueue(int bufferSize) {
		maxSize = bufferSize;
		ringBuffer = new DatagramPacket[maxSize];
	}

	public boolean isFull() {
		if (((tail + 1) % maxSize) == head) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmpty() {
		if (tail == head && tail == -1) {
			return true;
		} else {
			return false;
		}
	}

	public void enqueue(DatagramPacket data) { // should be datagram
		if (this.isFull() == true) {
			// throw overflow error
			System.out.println("Queue is full, cannot enqueue.");
		} else {

			if (tail == head && head == -1) {
				head += 1;
			}
			tail = (tail + 1) % maxSize;
			ringBuffer[tail] = data;
			System.out.println(data + "is enqueued.");
		}
	}

	public DatagramPacket dequeue() {
		if (this.isEmpty() == true) {
			// throw underflow error
			System.out.println("Queue is empty, cannot dequeue.");
			return null;
		} else {
			DatagramPacket data = ringBuffer[head];
			if (tail == head){
				tail = -1;
				head = -1;
			} else {
			head = (head + 1) % maxSize;
			}
			System.out.println(data + "is dequeued.");
			return data;
		}
	}

	public DatagramPacket peekHead() {
		if(this.isEmpty()) {
			System.out.println("Empty");
			return null;
		}
		else {
		DatagramPacket data = ringBuffer[head];
		return data;
		}
	}

	public DatagramPacket peek(int index) {
	       DatagramPacket data=ringBuffer[index];
	        
	        return data;
		}

	void print() {
		for (int i = head; i < tail; i++) {
			System.out.print(ringBuffer[i] + " ");
		}
		System.out.println();
	}

	// use main function to test

}


