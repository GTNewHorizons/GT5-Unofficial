package tectech.util;

import static gregtech.api.enums.GTValues.V;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;

/**
 * Created by Tec on 21.03.2017.
 */
public final class TTUtility {

    private TTUtility() {}

    private static final StringBuilder STRING_BUILDER = new StringBuilder();
    private static final Map<Locale, Formatter> FORMATTER_MAP = new HashMap<>();

    private static Formatter getFormatter() {
        STRING_BUILDER.setLength(0);
        return FORMATTER_MAP.computeIfAbsent(
            Locale.getDefault(Locale.Category.FORMAT),
            locale -> new Formatter(STRING_BUILDER, locale));
    }

    public static String formatNumberExp(double value) {
        return getFormatter().format("%+.5E", value)
            .toString();
    }

    public static String toExponentForm(BigInteger number) {
        BigInteger abs = number.abs();
        String strNum = abs.toString();
        int exponent = strNum.length() - 1;
        return (number.signum() == -1 ? "-" : "") + strNum.charAt(0) + "." + strNum.substring(1, 3) + "e" + exponent;

    }

    public static int bitStringToInt(String bits) {
        if (bits == null) {
            return 0;
        }
        if (bits.length() > 32) {
            throw new NumberFormatException("Too long!");
        }
        return Integer.parseInt(bits, 2);
    }

    public static int hexStringToInt(String hex) {
        if (hex == null) {
            return 0;
        }
        if (hex.length() > 8) {
            throw new NumberFormatException("Too long!");
        }
        return Integer.parseInt(hex, 16);
    }

    public static double stringToDouble(String str) {
        if (str == null) {
            return 0;
        }
        return Double.parseDouble(str);
    }

    public static String longBitsToShortString(long number) {
        StringBuilder result = new StringBuilder(71);

        for (int i = 63; i >= 0; i--) {
            long mask = 1L << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0) {
                result.append('|');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static String getUniqueIdentifier(ItemStack is) {
        return GameRegistry.findUniqueIdentifierFor(is.getItem()).modId + ':' + is.getUnlocalizedName();
    }

    public static byte getTier(long l) {
        byte b = -1;

        do {
            ++b;
            if (b >= V.length) {
                return b;
            }
        } while (l > V[b]);

        return b;
    }

    public static void setTier(int tier, Object me) {
        try {
            Field field = MTETieredMachineBlock.class.getField("mTier");
            field.setAccessible(true);
            field.set(me, (byte) tier);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Deprecated
    public static double receiveDouble(double previousValue, int startIndex, int index, int value) {
        return Double.longBitsToDouble(receiveLong(Double.doubleToLongBits(previousValue), startIndex, index, value));
    }

    public static long receiveLong(long previousValue, int startIndex, int index, int value) {
        value &= 0xFFFF;
        switch (index - startIndex) {
            case 0 -> {
                previousValue &= 0xFFFF_FFFF_FFFF_0000L;
                previousValue |= value;
            }
            case 1 -> {
                previousValue &= 0xFFFF_FFFF_0000_FFFFL;
                previousValue |= (long) value << 16;
            }
            case 2 -> {
                previousValue &= 0xFFFF_0000_FFFF_FFFFL;
                previousValue |= (long) value << 32;
            }
            case 3 -> {
                previousValue &= 0x0000_FFFF_FFFF_FFFFL;
                previousValue |= (long) value << 48;
            }
        }
        return previousValue;
    }

    @Deprecated
    public static float receiveFloat(float previousValue, int startIndex, int index, int value) {
        return Float.intBitsToFloat(receiveInteger(Float.floatToIntBits(previousValue), startIndex, index, value));
    }

    public static int receiveInteger(int previousValue, int startIndex, int index, int value) {
        value &= 0xFFFF;
        switch (index - startIndex) {
            case 0 -> {
                previousValue &= 0xFFFF_0000;
                previousValue |= value;
            }
            case 1 -> {
                previousValue &= 0x0000_FFFF;
                previousValue |= value << 16;
            }
        }
        return previousValue;
    }

    public static String[][] appendStringArrays(String[][] firstArray, String[][] secondArray) {
        int totalLength = firstArray.length + secondArray.length;
        String[][] resultArray = new String[totalLength][];

        System.arraycopy(firstArray, 0, resultArray, 0, firstArray.length);
        System.arraycopy(secondArray, 0, resultArray, firstArray.length, secondArray.length);
        return resultArray;
    }

    public static String[][] replaceLetters(String[][] array, String replacement) {
        String[][] outputArray = new String[array.length][];
        for (int i = 0; i < array.length; i++) {
            outputArray[i] = new String[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                outputArray[i][j] = array[i][j].replaceAll("[A-Z]", replacement);
            }
        }
        return outputArray;
    }
}
