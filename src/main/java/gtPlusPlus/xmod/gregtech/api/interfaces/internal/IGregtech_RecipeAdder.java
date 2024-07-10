package gtPlusPlus.xmod.gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.Material;

@SuppressWarnings("UnusedReturnValue")
public interface IGregtech_RecipeAdder {

    /**
     * Adds a custom Semifluid fuel for the GT++ SemiFluid Generators.
     *
     * @param aFuelItem  - A Fluidstack to be consumed.
     * @param aFuelValue - Fuel value in thousands (1 = 1000)
     * @return - Was the Fuel added?
     */
    boolean addSemifluidFuel(FluidStack aFuelItem, int aFuelValue);

    /**
     * Adds a custom Semifluid fuel for the GT++ SemiFluid Generators.
     *
     * @param aFuelItem  - A Fluidstack to be consumed.
     * @param aFuelValue - Fuel value in thousands (1 = 1000)
     * @return - Was the Fuel added?
     */
    boolean addSemifluidFuel(ItemStack aFuelItem, int aFuelValue);

    boolean addMillingRecipe(Material aMat, int aEU);

    boolean addFlotationRecipe(Materials aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU);

    boolean addFlotationRecipe(Material aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU);

    boolean addFuelForRTG(ItemStack aFuelPellet, int aFuelDays, int aVoltage);
}
