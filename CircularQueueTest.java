package Project1.cs488.pace.edu;
public class CircularQueueTest {

	int maxSize;
	int head = -1;
	int tail = -1;
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
		if (tail == head && tail == -1) {
			return true;
		} else {
			return false;
		}
	}

	public void enqueue(int data) { // should be datagram
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

	public int dequeue() {
		if (this.isEmpty() == true) {
			// throw underflow error
			System.out.println("Queue is empty, cannot dequeue.");
			return -1;
		} else {
			int data = ringBuffer[head];
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

	public int peekHead() {
		if(this.isEmpty()) {
			System.out.println("Empty");
			return -1;
		}
		else {
		int data = ringBuffer[head];
		return data;
		}
	}

	void print() {
		for (int i = head; i < tail; i++) {
			System.out.print(ringBuffer[i] + " ");
		}
		System.out.println();
	}

	public static void main(String args[]) {

		CircularQueueTest buffer = new CircularQueueTest(3);

		buffer.enqueue(1);
		buffer.print();
		buffer.enqueue(2);
		buffer.print();
		buffer.enqueue(3);
		buffer.print();
		System.out.println(buffer.peekHead());
		buffer.dequeue();
		buffer.print();
		buffer.dequeue();
		buffer.print();
		System.out.println(buffer.peekHead());
		buffer.dequeue();
		buffer.print();
		
	}

}
package Project1.cs488.pace.edu;
public class CircularQueueTest {

	int maxSize;
	int head = -1;
	int tail = -1;
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
		if (tail == head && tail == -1) {
			return true;
		} else {
			return false;
		}
	}

	public void enqueue(int data) { // should be datagram
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

	public int dequeue() {
		if (this.isEmpty() == true) {
			// throw underflow error
			System.out.println("Queue is empty, cannot dequeue.");
			return -1;
		} else {
			int data = ringBuffer[head];
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

	public int peekHead() {
		if(this.isEmpty()) {
			System.out.println("Empty");
			return -1;
		}
		else {
		int data = ringBuffer[head];
		return data;
		}
	}

	void print() {
		for (int i = head; i < tail; i++) {
			System.out.print(ringBuffer[i] + " ");
		}
		System.out.println();
	}

	public static void main(String args[]) {

		CircularQueueTest buffer = new CircularQueueTest(3);

		buffer.enqueue(1);
		buffer.print();
		buffer.enqueue(2);
		buffer.print();
		buffer.enqueue(3);
		buffer.print();
		System.out.println(buffer.peekHead());
		buffer.dequeue();
		buffer.print();
		buffer.dequeue();
		buffer.print();
		System.out.println(buffer.peekHead());
		buffer.dequeue();
		buffer.print();
		
	}

}
