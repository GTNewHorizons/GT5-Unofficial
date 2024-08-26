package pers.gwyog.gtneioreplugin.util;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.google.common.base.Strings;

public class StringPaddingHack {

    private static final int SPACE_WIDTH = 4;
    private static final int BOLD_SPACE_WIDTH = 5;

    /**
     * Given a list of strings, arrange them into the requested number of columns with the specified spacing. Up to 3
     * additional spaces might be added between columns because this function relies on quirky font behaviors.
     *
     * @param strings          List of strings to wrap into columns
     * @param numColumns       Number of columns, minimum of 1
     * @param minColumnSpacing Minimum amount of extra spaces between columns.
     * @return strings wrapped into columns
     */
    public static String[] stringsToSpacedColumns(String[] strings, int numColumns, int minColumnSpacing) {
        if (numColumns < 1) {
            throw new IllegalArgumentException(
                String.format("Argument numColumns must be 1 or higher, got value %d", numColumns));
        }
        if (numColumns > 1) {
            int sliceSize = strings.length / numColumns;
            int remainder = strings.length % numColumns;
            String[][] columns = new String[numColumns][];
            int totalExtra = 0;

            // Arrange all strings into their proper columns so that the list of strings wraps through all columns
            for (int i = 0; i < numColumns; i++) {
                int extra = 0;
                if (remainder > 0) {
                    remainder--;
                    extra = 1;
                }
                columns[i] = Arrays
                    .copyOfRange(strings, (sliceSize * i) + totalExtra, (sliceSize * (i + 1) + totalExtra + extra));

                totalExtra += extra;
            }

            // Add extra padding to all but the last columns to align the text
            for (int i = 0; i < numColumns - 1; i++) {
                columns[i] = padStrings(columns[i], minColumnSpacing);
            }

            // Concatenate all columns into the final result
            strings = columns[0];
            for (int i = 0; i < sliceSize; i++) {
                for (int j = 1; j < numColumns; j++) {
                    strings[i] += columns[j][i];
                }
            }
        }

        return strings;
    }

    /**
     * Pads strings with spaces so that they are of equal length and adds to that the number of spaces specified and up
     * to 3 if minExtraSpaces is below 3. Added spaces might be bold.
     * <p>
     * Relies on the quirk of bold space characters being 1 pixel wider than regular space characters in the default
     * font renderer.
     *
     * @param strings        List of strings
     * @param minExtraSpaces The minimum number of extra spaces to add
     * @return Array of strings padded with spaces to an equal length
     */
    public static String[] padStrings(String[] strings, int minExtraSpaces) {
        int[] widths = getStringWidths(strings);
        int maxUnPaddedStrLength = 0;
        int numSpacesAddedToLongestString = 0;
        int maxPaddedStrLength = 0;

        // Making string width a multiple of 4 by adding bold spaces of width 5
        for (int i = 0; i < strings.length; i++) {
            int mod = widths[i] % SPACE_WIDTH;
            int numBoldSpacesToAdd = (SPACE_WIDTH - mod) % SPACE_WIDTH;

            // Keep track of the number of spaces added to the longest string
            if (widths[i] > maxUnPaddedStrLength) {
                numSpacesAddedToLongestString = numBoldSpacesToAdd;
                maxUnPaddedStrLength = widths[i];
            }

            strings[i] += "§l" + Strings.repeat(" ", numBoldSpacesToAdd) + "§r";
            widths[i] += numBoldSpacesToAdd * BOLD_SPACE_WIDTH;

            // Keep track of the current widest string we currently have
            if (widths[i] > maxPaddedStrLength) {
                maxPaddedStrLength = widths[i];
            }
        }

        // Make sure we pad at least up to the desired number of spaces from the longest string
        if (numSpacesAddedToLongestString < minExtraSpaces) {
            maxPaddedStrLength += (minExtraSpaces - numSpacesAddedToLongestString) * SPACE_WIDTH;
        }

        // Add required spaces to equalize length of all strings to at least the target width
        for (int i = 0; i < strings.length; i++) {
            int numSpacesToAdd = (maxPaddedStrLength - widths[i]) / SPACE_WIDTH;
            strings[i] += Strings.repeat(" ", numSpacesToAdd);
            widths[i] += numSpacesToAdd * SPACE_WIDTH;
        }

        return strings;
    }

    /**
     * Returns an array of font widths for the given array of strings
     *
     * @param strList Array of strings
     * @return Array of font widths
     */
    protected static int[] getStringWidths(String[] strList) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int[] widths = new int[strList.length];
        for (int i = 0; i < strList.length; ++i) {
            widths[i] = font.getStringWidth(strList[i]);
        }
        return widths;
    }
}
