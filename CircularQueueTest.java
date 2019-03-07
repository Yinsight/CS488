public class CircularQueueTest {

	int maxSize;
	int head = 0;
	int tail = 0;
	int ringBuffer[];
	// need to change abstract type later

	public CircularQueueTest(int bufferSize) {
		maxSize = bufferSize;
		ringBuffer = new int[maxSize];
	}

	public boolean isFull() {
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

	public void enqueue(int data) { // should be datagram
		if (this.isFull() == true) {
			// throw overflow error
			System.out.println("Queue is full, cannot enqueue.");
		} else if (this.isEmpty() == true) {
			ringBuffer[head] = data;
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

	public int peekHead() {
		int data = ringBuffer[head];
		return data;
	}

	public static void main(String args[]) {

		CircularQueueTest buffer = new CircularQueueTest(3);

		buffer.enqueue(1);
		buffer.enqueue(2);
		buffer.enqueue(3);

		System.out.println(buffer);
	}
}
