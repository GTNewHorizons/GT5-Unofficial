package tectech.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Converter {

    private Converter() {}

    public static byte[] writeInts(int[] array) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(array.length * 4);
            DataOutputStream dos = new DataOutputStream(bos);
            for (int j : array) {
                dos.writeInt(j);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] readInts(byte[] array) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            DataInputStream dataInputStream = new DataInputStream(bis);
            int size = array.length / Integer.BYTES;
            int[] res = new int[size];
            for (int i = 0; i < size; i++) {
                res[i] = dataInputStream.readInt();
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
