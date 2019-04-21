package lab1.cs488.pace.edu;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Receiver {

    static int targetPort; // get port from received packet
    final static int ownPort = 8888;
    static int seqNum = 0;                 // datagram sequence number
    static InetAddress host = null;


    public static void main(String[] args) throws IOException {

        // Initializing buffer of size 3
        CircularQueue<byte[]> buffer = new CircularQueue<byte[]>(3);

        File file = new File("./Resource/copy_1_udp.jpg");
        FileOutputStream fis = new FileOutputStream(file);

        byte[] fileData = new byte[1024]; //size of file data
        DatagramSocket datagramSocketL = new DatagramSocket(ownPort);    //to receive datagrams
        DatagramSocket datagramSocketS = new DatagramSocket();                 // to send ack
        DatagramSocket datagramSocketR = new DatagramSocket();
        datagramSocketL.setSoTimeout(10000);
        System.out.println("Receiver: Listening");

        int expected = 1;

        try {
            host = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //boolean running = true;

        boolean stop = false;


        while (true) {
            while (!buffer.isFull()) {
                System.out.println("Enter While loop");
                try {
                    byte[] data = new byte[1028]; //size of seq + file data
                    DatagramPacket receivePacket = new DatagramPacket(data, data.length);    // incoming packet
                    datagramSocketL.receive(receivePacket);            // get data
                    host = receivePacket.getAddress();
                    targetPort = receivePacket.getPort();

                    // break down of data - get sequence number
                    // needs to match how sender manages the sequence Number
                    System.out.println("Sequence number to be generated");
                    // Do the reverse process of the sender to get sequence Number
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                    DataInputStream dataInput = new DataInputStream(inputStream);
                    seqNum = dataInput.readInt();
                    System.out.println("Receiver: Received sequence number: " + seqNum);

                    stop = seqNum == -1; // Stop if last packet has been received

                    if (expected == seqNum) {
                        System.out.println("Yes");
                        int drop = random.nextInt(5); //Packet drop simulation: drop packet when random number is 3
                        if(drop == 3) {
                            System.out.println("Dropping packet " + seqNum); 
                        } else {                     
                        buffer.enqueue(data);        // make changes to circular queue
                        expected++;
                    }

                    if (stop) {
                        break;
                    }

                    //add to buffer (Circular Queue)


                } catch (SocketTimeoutException e) {
                    break;
                }

            }

            if (stop) {
                break;
            }

            for (int i = 0; i < buffer.maxSize; i++) {
                //check if packet was received and send ack
                byte[] check;
                check = (byte[]) buffer.peek(i);


                int v = bytesToInt(copyOfRange(check, 0, 4));
                System.out.println(v);

                // Send ack for all datagrams that are valid
                if (v != -1) {
                    // Recover seqNum from datagram and send ack
                    byte[] ackNumBytes = copyOfRange(check, 0, 4);
                    DatagramPacket ackPacket = new DatagramPacket(ackNumBytes, ackNumBytes.length, host, targetPort);
                    datagramSocketS.send(ackPacket);
                    System.out.println("Receiver: Sent Acknowledgement " + ByteBuffer.wrap(copyOfRange(check, 0, 4)).getInt());
                }

            }

            byte[] checkn;
            byte[] check;
            check = (byte[]) buffer.peekHead();
            if (check != null) {
                int v = bytesToInt(check);

                if (v == -1) {
                    break;
                }

                while (!(buffer.isEmpty()) && v != -1) {
                    checkn = (byte[]) buffer.dequeue();
                    fis.write(checkn);
                    if (!buffer.isEmpty()) {
                        checkn = (byte[]) buffer.peekHead();
                        v = bytesToInt(checkn);

                    }
                }
            }

        }

        datagramSocketL.close();
        datagramSocketS.close();
        fis.close();

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
