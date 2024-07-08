package gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;

public interface IGT_RecipeAdder {
    /**
     * Adds a Forge Hammer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addWiremillRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Extruder Machine Recipe
     *
     * @param aInput    must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Distillation Tower Recipe
     *
     * @param aInput   must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    @Deprecated
    boolean addDistillationTowerRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration,
        int aEUt);

    /**
     * Adds a Recipe for the Distillery
     */
    @Deprecated
    boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput,
        int aDuration, int aEUt, boolean aHidden);

    @Deprecated
    boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt,
        boolean aHidden);

    /**
     * Adds a Recipe for the Fluid Solidifier
     */
    @Deprecated
    boolean addFluidSolidifierRecipe(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Chemical Bath
     */
    @Deprecated
    boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Electromagnetic Separator
     */
    @Deprecated
    boolean addElectromagneticSeparatorRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Extractor
     */
    @Deprecated
    boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Mixer
     */
    @Deprecated
    boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);

    @Deprecated
    boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput,
        int aDuration, int aEUt);

    @Deprecated
    boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);


    /**
     * Adds a Recipe for the Sifter. (up to 9 Outputs)
     */
    @Deprecated
    boolean addSifterRecipe(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt);

    /**
     * Add a breeder cell.
     *
     * @param input          raw stack. should be undamaged.
     * @param output         breed output
     * @param heatMultiplier bonus progress per neutron pulse per heat step
     * @param heatStep       divisor for hull heat
     * @param reflector      true if also acts as a neutron reflector, false otherwise.
     * @param requiredPulses progress required to complete breeding
     * @return added fake recipe
     */
    GT_Recipe addIC2ReactorBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
        int heatMultiplier, int requiredPulses);

    /**
     * Add a fuel cell.
     *
     * @param input   raw stack. should be undamaged.
     * @param output  depleted stack
     * @param aMox    true if has mox behavior, false if uranium behavior.
     * @param aHeat   inherent heat output multiplier of the fuel material. should not add the extra heat from being a
     *                multi-cell!
     * @param aEnergy inherent energy output multiplier of the fuel material. should not add the extra energy from being
     *                a multi-cell!
     * @param aCells  cell count
     * @return added fake recipe
     */
    GT_Recipe addIC2ReactorFuelCell(ItemStack input, ItemStack output, boolean aMox, float aHeat, float aEnergy,
        int aCells);

    GT_RecipeBuilder stdBuilder();
}
