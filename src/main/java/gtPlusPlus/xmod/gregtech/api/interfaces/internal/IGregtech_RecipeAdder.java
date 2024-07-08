package gtPlusPlus.xmod.gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.Material;

@SuppressWarnings("UnusedReturnValue")
public interface IGregtech_RecipeAdder {

    /**
     * Adds a Recipe for the Quantum Force Smelter (up to 9 Inputs)
     *
     **/
    boolean addQuantumTransformerRecipe(ItemStack[] aInput, FluidStack[] aFluidInput, FluidStack[] aFluidOutput,
        ItemStack[] aOutputStack, int[] aChances, int aDuration, int aEUt, int aSpecialValue);

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

    boolean addFissionFuel(FluidStack aInput1, FluidStack aInput2, FluidStack aInput3, FluidStack aInput4,
        FluidStack aInput5, FluidStack aInput6, FluidStack aInput7, FluidStack aInput8, FluidStack aInput9,
        FluidStack aOutput1, FluidStack aOutput2, int aDuration, int aEUt);

    boolean addFissionFuel(boolean aOptimise, FluidStack aInput1, FluidStack aInput2, FluidStack aInput3,
        FluidStack aInput4, FluidStack aInput5, FluidStack aInput6, FluidStack aInput7, FluidStack aInput8,
        FluidStack aInput9, FluidStack aOutput1, FluidStack aOutput2, int aDuration, int aEUt);

    boolean addCyclotronRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, FluidStack aFluidOutput,
        int[] aChances, int aDuration, int aEUt, int aSpecialValue);

    boolean addCyclotronRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack[] aOutput,
        FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue);

    @Deprecated
    boolean addBrewingRecipe(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aTime, int aEu,
        boolean aHidden);

    @Deprecated
    boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aDust, ItemStack aOutput);

    @Deprecated
    boolean addVacuumFurnaceRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aLevel);

    boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int time, long eu, int aTier);

    boolean addChemicalPlantRecipe(ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int[] aChances, int time, long eu, int aTier);

    @Deprecated
    boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput,
        int aDuration, int aEUt, boolean aHidden);

    boolean addMillingRecipe(Material aMat, int aEU);

    boolean addFlotationRecipe(Materials aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU);

    boolean addFlotationRecipe(Material aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU);

    @Deprecated
    boolean addpackagerRecipe(ItemStack aRecipeType, ItemStack aInput1, ItemStack aInput2, ItemStack aOutputStack1);

    boolean addFuelForRTG(ItemStack aFuelPellet, int aFuelDays, int aVoltage);

    boolean addColdTrapRecipe(int aCircuit, ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        int[] aChances, FluidStack aFluidOutput, int aTime, int aEU);

    boolean addReactorProcessingUnitRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        ItemStack[] aOutputs, int[] aChances, FluidStack aFluidOutput, int aTime, int aEU);

    @Deprecated
    boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);

    @Deprecated
    boolean addVacuumFreezerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEU);

    boolean addMolecularTransformerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEU);

    boolean addMolecularTransformerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEU, int aAmps);
}
