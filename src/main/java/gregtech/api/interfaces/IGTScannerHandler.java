package gregtech.api.interfaces;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTScannerResult;
import gregtech.common.tileentities.machines.basic.MTEScanner;

public interface IGTScannerHandler {

    @Nullable
    GTScannerResult apply(@Nonnull MTEScanner aScanner, @Nonnull ItemStack aInput, @Nullable ItemStack aSpecialSlot,
        @Nullable FluidStack aFluid);
}
