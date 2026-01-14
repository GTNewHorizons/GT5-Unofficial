package gregtech.api.interfaces;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTScannerResult;

@FunctionalInterface
public interface IGTScannerHandler {

    @Nullable
    GTScannerResult apply(@Nonnull MetaTileEntity aScanner, @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot,
        @Nullable FluidStack aFluid);
}
