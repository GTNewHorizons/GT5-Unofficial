package tectech.util;

import java.lang.reflect.Field;
import java.math.BigInteger;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;

/**
 * Created by Tec on 21.03.2017.
 */
public final class TTUtility {

    private TTUtility() {}

    public static String toExponentForm(BigInteger number) {
        BigInteger abs = number.abs();
        String strNum = abs.toString();
        int exponent = strNum.length() - 1;
        return (number.signum() == -1 ? "-" : "") + strNum
            .charAt(0) + "." + getExponentFormDecimalPart(strNum) + "e" + exponent;
    }

    public static String toExponentForm(long number) {
        long abs = Math.abs(number);
        String strNum = Long.toString(abs);
        int exponent = strNum.length() - 1;
        return (Long.signum(number) == -1 ? "-" : "") + strNum
            .charAt(0) + "." + getExponentFormDecimalPart(strNum) + "e" + exponent;
    }

    private static String getExponentFormDecimalPart(String strNum) {
        var s = StringUtils.substring(strNum, 1, 3);
        return StringUtils.rightPad(s, 2, "0");
    }

    public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static String getUniqueIdentifier(ItemStack is) {
        return GameRegistry.findUniqueIdentifierFor(is.getItem()).modId + ':' + is.getUnlocalizedName();
    }

    public static void setTier(int tier, Object o) {
        // TODO why is it using reflection to change a final field from GREGTECH ?
        if (!(o instanceof MTETieredMachineBlock)) {
            GTMod.GT_FML_LOGGER.error(
                "Could not set tier as object " + o.getClass()
                    .getName() + " isn't instance of MTETieredMachineBlock");
            return;
        }
        try {
            Field field = MTETieredMachineBlock.class.getField("mTier");
            field.setAccessible(true);
            field.set(o, (byte) tier);
        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error(
                "Could not set tier of " + o.getClass()
                    .getName(),
                e);
        }
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
