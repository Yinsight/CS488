package lab1.cs488.pace.edu;

import java.util.Arrays;

public class CircularQueue {

	static int maxSize;
	static int head = 0;
	static int tail = 0;
	static int ringBuffer[];
	// need to change abstract type later

	public CircularQueue(int bufferSize) {
		maxSize = bufferSize;
		ringBuffer = new int[maxSize];
	}

	public static boolean isFull() {
		if (((tail + 1) % maxSize) == head) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmpty() {
		if (tail == head) {
			return true;
		} else {
			return false;
		}
	}

	public void enqueue(int data) { //should be datagram
		if (this.isFull() == true) {
			// throw overflow error
			System.out.println("Queue is full, cannot enqueue.");
		} else {
			tail = (tail + 1) % maxSize;
			ringBuffer[tail] = data;
			System.out.println(data + "is enqueued.");
		}
	}

	public int dequeue() {
		if (this.isEmpty() == true) {
			// throw underflow error
			System.out.println("Queue is empty, cannot dequeue.");
			return -1;
		} else {
			int data = ringBuffer[head];
			head = (head + 1) % maxSize;
			System.out.println(data + "is dequeued.");
			return data;
		}
	}
	
	public static int peekHead(){
		int data = ringBuffer[head];
		return data;
	}

	// use main function to test

}
