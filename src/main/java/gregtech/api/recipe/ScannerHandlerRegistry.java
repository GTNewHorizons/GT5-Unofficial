package gregtech.api.recipe;

import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.IGTScannerHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTScannerResult;

public class ScannerHandlerRegistry extends LinkedList<IGTScannerHandler> {

    /**
     * Tries to find a scan result.
     *
     * @param aScanner     The MTE executing the request.
     * @param aInput       The stack in the input slot.
     * @param aSpecialSlot The stack in the special slot.
     * @param aFluid       The fluid in the MTE
     * @return The result of the query.
     */
    public GTScannerResult findRecipe(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput,
        @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        return this.findRecipeWithCache(null, aScanner, aInput, aSpecialSlot, aFluid);
    }

    /**
     * Tries to find a scan result.
     *
     * @param aCached      A cached handler.
     * @param aScanner     The MTE executing the request.
     * @param aInput       The stack in the input slot.
     * @param aSpecialSlot The stack in the special slot.
     * @param aFluid       The fluid in the MTE
     * @return The result of the query.
     */
    public GTScannerResult findRecipeWithCache(@Nullable IGTScannerHandler aCached, @Nonnull MetaTileEntity aScanner,
        @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot, @Nullable FluidStack aFluid) {
        if (aCached != null) {
            GTScannerResult result = aCached.apply(aScanner, aInput, aSpecialSlot, aFluid);
            if (result != null) {
                result.handler = aCached;
                return result;
            }
        }
        for (IGTScannerHandler handler : this) {
            if (handler == null || handler == aCached) continue;
            GTScannerResult result = handler.apply(aScanner, aInput, aSpecialSlot, aFluid);
            // check next if not handled
            if (result == null) continue;
            result.handler = handler;
            return result;
        }
        return null;
    }
}
