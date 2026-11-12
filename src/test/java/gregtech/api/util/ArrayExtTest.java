package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gregtech.api.util.extensions.ArrayExt;

public class ArrayExtTest {

    @Test
    void testFixChancesArray() {

        assertNull(ArrayExt.fixChancesArray(null, -1000));
        assertNull(ArrayExt.fixChancesArray(null, -1));
        assertNull(ArrayExt.fixChancesArray(null, 42));

        assertNull(ArrayExt.fixChancesArray(new int[0], -1000));
        assertNull(ArrayExt.fixChancesArray(new int[0], -1));
        assertNull(ArrayExt.fixChancesArray(new int[0], 0));
        assertNull(ArrayExt.fixChancesArray(new int[0], 42));

        assertNull(ArrayExt.fixChancesArray(new int[] { -1, -511, 15000, 10001, 10000 }, -1000));
        assertNull(ArrayExt.fixChancesArray(new int[] { -1, -511, 15000, 10001, 10000 }, -1));
        assertNull(ArrayExt.fixChancesArray(new int[] { -1, -511, 15000, 10001, 10000 }, 5));
        assertNull(ArrayExt.fixChancesArray(new int[] { -1, -511, 15000, 10001, 10000 }, 42));

        int[] expected, actual;

        expected = new int[] { 5, 5, 5, 5 };
        actual = ArrayExt.fixChancesArray(new int[] { 5, 5, 5, 5 }, -1);
        assertArrayEquals(expected, actual);

        expected = new int[] { 5, 5, 5, 5 };
        actual = ArrayExt.fixChancesArray(new int[] { 5, 5, 5, 5 }, 4);
        assertArrayEquals(expected, actual);

        expected = new int[] { 5, 5, 5, 5, 10000, 10000, 10000, 10000 };
        actual = ArrayExt.fixChancesArray(new int[] { 5, 5, 5, 5 }, 8);
        assertArrayEquals(expected, actual);

        expected = new int[] { 10000, 10000, 10000, 10000, 5, 5, 5, 5 };
        actual = ArrayExt.fixChancesArray(new int[] { -1, -1, -1, -1, 5, 5, 5, 5 }, -1);
        assertArrayEquals(expected, actual);

        assertNull(ArrayExt.fixChancesArray(new int[] { -1, -1, -1, -1, 5, 5, 5, 5 }, 3));

    }

}
