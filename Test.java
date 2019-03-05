import java.util.Arrays;

public class Test {

	public static void main(String args[]) {

		byte[] data = new byte[2];
		byte[] indexinbyte = new byte[4];
		byte[] datawithSeq = new byte[6];

		data[0] = 2;
		data[1] = 1;
		
		int index = 7800;
		
		indexinbyte[0] = (byte) (index >>> 24);
		indexinbyte[1] = (byte) (index >>> 16);
		indexinbyte[2] = (byte) (index >>> 8);
		indexinbyte[3] = (byte) (index);
			
	/*
		for (int index = 0; index < 1; index++) {
			indexinbyte[0] = (byte) (index >>> 24);
			indexinbyte[1] = (byte) (index >>> 16);
			indexinbyte[2] = (byte) (index >>> 8);
			indexinbyte[3] = (byte) (index);
		}
		*/

		System.arraycopy(data, 0, datawithSeq, 0, 2);
		System.arraycopy(indexinbyte, 0, datawithSeq, 2, 4);

		System.out.println("Resulting array =" + Arrays.toString(datawithSeq));
	}

}
