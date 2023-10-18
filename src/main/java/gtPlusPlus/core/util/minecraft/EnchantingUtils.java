package gtPlusPlus.core.util.minecraft;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.api.objects.Logger;

public class EnchantingUtils {

    // Xp Fluids
    public static FluidStack getMobEssence(final int amount) {
        Logger.WARNING("Trying to get a fluid stack of Mob Essence.");
        try {
            return FluidRegistry.getFluidStack("mobessence", amount).copy();
        } catch (final Throwable e) {
            return null;
        }
    }

    public static FluidStack getLiquidXP(final int amount) {
        Logger.WARNING("Trying to get a fluid stack of Liquid XP.");
        try {
            return FluidRegistry.getFluidStack("xpjuice", amount).copy();
        } catch (final Throwable e) {
            return null;
        }
    }

}
