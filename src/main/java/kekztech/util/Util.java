package kekztech.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import kekztech.common.items.ErrorItem;

public class Util {

    protected static final DecimalFormat percentFormatRound_6 = new DecimalFormat("0.000000%");
    protected static final DecimalFormat percentFormatRound_2 = new DecimalFormat("0.00%");
    protected static final BigDecimal Threshold_1 = BigDecimal.valueOf(0.01);
    protected static DecimalFormat standardFormat;

    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("x10^");
        standardFormat = new DecimalFormat("0.00E0", dfs);
    }

    public static ItemStack getStackofAmountFromOreDict(String oredictName, final int amount) {
        final ArrayList<ItemStack> list = OreDictionary.getOres(oredictName);
        if (!list.isEmpty()) {
            final ItemStack ret = list.get(0)
                .copy();
            ret.stackSize = amount;
            return ret;
        }
        System.err.println("Failed to find " + oredictName + " in OreDict");
        return new ItemStack(ErrorItem.getInstance(), amount);
    }

    public static ItemStack[] toItemStackArray(List<ItemStack> stacksList) {
        if (stacksList.size() == 0) {
            return null;
        }

        ItemStack[] ret = new ItemStack[stacksList.size()];
        Iterator<ItemStack> iterator = stacksList.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next();
        }
        return ret;
    }

    public static FluidStack[] toFluidStackArray(List<FluidStack> stacksList) {
        if (stacksList.size() == 0) {
            return null;
        }

        FluidStack[] ret = new FluidStack[stacksList.size()];
        Iterator<FluidStack> iterator = stacksList.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next();
        }
        return ret;
    }

    /* If the number is less than 1, we round by the 6, otherwise to 2 */
    public static String toPercentageFrom(BigInteger value, BigInteger maxValue) {
        if (BigInteger.ZERO.equals(maxValue)) {
            return "0.00%";
        }
        BigDecimal result = new BigDecimal(value).setScale(6, RoundingMode.HALF_UP)
            .divide(new BigDecimal(maxValue), RoundingMode.HALF_UP);
        if (result.compareTo(Threshold_1) < 0) {
            return percentFormatRound_6.format(result);
        } else {
            return percentFormatRound_2.format(result);
        }
    }

    /* Get a string like this: 4.56*10^25 */
    public static String toStandardForm(BigInteger number) {
        if (BigInteger.ZERO.equals(number)) {
            return "0";
        }

        return standardFormat.format(number);
    }

}
