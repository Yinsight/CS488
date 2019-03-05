import java.nio.ByteBuffer;
import java.util.Arrays;

public class Test {

	public static void main(String args[]) {

		byte[] data = new byte[2];
		byte[] indexinbyte = new byte[4];
		byte[] datawithSeq = new byte[6];

		data[0] = 2;
		data[1] = 1;

		int index = 0x01234567;

		indexinbyte[0] = (byte) (index >>> 24);
		indexinbyte[1] = (byte) (index >>> 16);
		indexinbyte[2] = (byte) (index >>> 8);
		indexinbyte[3] = (byte) (index);

		System.arraycopy(data, 0, datawithSeq, 0, 2);
		System.arraycopy(indexinbyte, 0, datawithSeq, 2, 4);

		System.out.println(String.format("0x%08x", (int) indexinbyte[0]));
		System.out.println(String.format("0x%08x", (int) indexinbyte[1]));
		System.out.println(String.format("0x%08x", (int) indexinbyte[2]));
		System.out.println(String.format("0x%08x", (int) indexinbyte[3]));

		int seqNum = ByteBuffer.wrap(copyOfRange(datawithSeq, datawithSeq.length - 4, datawithSeq.length)).getInt();

		System.out.println(String.format("0x%08x", seqNum));

	}

	public static byte[] copyOfRange(byte[] srcArr, int start, int end) {
		int length = (end > srcArr.length) ? srcArr.length - start : end - start;
		byte[] seqArr = new byte[length];
		System.arraycopy(srcArr, start, seqArr, 0, length);
		return seqArr;
	}

}
