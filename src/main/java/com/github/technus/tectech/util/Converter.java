package com.github.technus.tectech.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Converter {

    private Converter() {}

    public static void writeInts(int[] array, ByteArrayOutputStream byteArrayOutputStream) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            for (int i = 0; i < array.length; i++) {
                dataOutputStream.writeInt(array[i]);
            }
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readInts(ByteArrayInputStream byteArrayInputStream, int[] array) {
        try {
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            for (int i = 0; i < array.length; i++) {
                array[i] = dataInputStream.readInt();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] writeInts(int[] array) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(array.length * 4);
            DataOutputStream dos = new DataOutputStream(bos);
            for (int i = 0; i < array.length; i++) {
                dos.writeInt(array[i]);
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
