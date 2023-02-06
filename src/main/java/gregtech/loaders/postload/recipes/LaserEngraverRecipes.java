package gregtech.loaders.postload.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addLaserEngraverRecipe(
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.ExcitedDTCC.getFluid(1000L) },
                50_000,
                125_000,
                true);

        GT_Values.RA.addLaserEngraverRecipe(
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.ExcitedDTPC.getFluid(1000L) },
                50_000,
                125_000 * 4,
                true);

        GT_Values.RA.addLaserEngraverRecipe(
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.ExcitedDTRC.getFluid(1000L) },
                50_000,
                125_000 * 16,
                true);

        GT_Values.RA.addLaserEngraverRecipe(
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentExoticCatalyst.getFluid(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.ExcitedDTEC.getFluid(1000L) },
                50_000,
                125_000 * 64,
                true);
    }
}
