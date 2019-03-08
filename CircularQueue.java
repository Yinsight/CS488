package lab1.cs488.pace.edu;

public class CircularQueue<T> {

	int maxSize;
	int head = -1;
	int tail = -1;
	Object[] ringBuffer;
	// need to change abstract type later

	public CircularQueue(int bufferSize) {
		maxSize = bufferSize;
		ringBuffer = new Object[maxSize];
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

	public void enqueue(T data) { // should be datagram
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

	public Object dequeue() {
		if (this.isEmpty() == true) {
			// throw underflow error
			System.out.println("Queue is empty, cannot dequeue.");
			return null;
		} else {
			Object data = ringBuffer[head];
			if (tail == head) {
				tail = -1;
				head = -1;
			} else {
				head = (head + 1) % maxSize;
			}
			System.out.println(data + "is dequeued.");
			return data;
		}
	}

	public Object peekHead() {
		if (this.isEmpty()) {
			System.out.println("Empty");
			return null;
		} else {
			Object data = ringBuffer[head];
			return data;
		}
	}

	public Object peek(int index) {
		Object data = ringBuffer[index];

		return data;
	}

	void print() {
		for (int i = head; i < tail; i++) {
			System.out.print(ringBuffer[i] + " ");
		}
		System.out.println();
	}

}
