package gtPlusPlus.core.material;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.bartworks.BWUtils;

public class MaterialStack {

    private final transient int[] vAmount;
    private final Material stackMaterial;
    private final double vPercentageToUse;

    public MaterialStack(final Material inputs, final double partOutOf100) {
        this.stackMaterial = inputs;
        this.vPercentageToUse = partOutOf100;
        this.vAmount = this.math(partOutOf100);
    }

    private int[] math(final double val) {
        double i;
        // Cast to a BigDecimal to round it.
        final BigDecimal bd = new BigDecimal(val).setScale(2, RoundingMode.HALF_EVEN);
        i = bd.doubleValue();
        // Split the string into xx.xx
        final String[] arr = String.valueOf(i)
            .split("\\.");
        final int[] intArr = new int[2];
        intArr[0] = Integer.parseInt(arr[0]);
        intArr[1] = Integer.parseInt(arr[1]);
        return intArr;
    }

    public ItemStack getValidStack() {
        if (this.stackMaterial.getDust(1) == null) {
            return null;
        }
        return this.stackMaterial.getDust(this.vAmount[0]);
    }

    public ItemStack getDustStack() {
        return this.stackMaterial.getDust(this.vAmount[0]);
    }

    public ItemStack getDustStack(final int amount) {
        return this.stackMaterial.getDust(amount);
    }

    public ItemStack getUnificatedDustStack(final int amount) {
        if (this.stackMaterial.werkstoffID != 0) {
            ItemStack stack = BWUtils
                .getCorrespondingItemStack(OrePrefixes.dust, this.stackMaterial.werkstoffID, amount);
            if (stack != null) {
                return stack;
            }
        }
        return getDustStack(amount);
    }

    public Material getStackMaterial() {
        if (this.stackMaterial == null) {
            Logger.modLogger.error("Tried getStackMaterial, got an invalid material.", new Exception());
            return null;
        }
        return this.stackMaterial;
    }

    public int getPartsPerOneHundred() {
        if (this.vAmount != null) {
            if ((this.vAmount[0] >= 1) && (this.vAmount[0] <= 100)) {
                return this.vAmount[0];
            }
        }
        return 100;
    }
}
