package gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;

public interface IGT_RecipeAdder {

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aInput2   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds an Assembler Recipe
     *
     * @param aInputs   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     *
     */
    @Deprecated
    boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration,
        int aEUt);

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt);

    @Deprecated
    boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt,
        boolean aCleanroom);

    /**
     * Adds an Assemblyline Recipe
     *
     * @param aInputs      must be != null, 4-16 inputs
     * @param aFluidInputs 0-4 fluids
     * @param aOutput1     must be != null
     * @param aDuration    must be > 0
     * @param aEUt         should be > 0
     */
    @Deprecated
    boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Assemblyline Recipe
     *
     * @param aInputs elements should be: ItemStack for single item; ItemStack[] for multiple equivalent items;
     *                {OreDict, amount} for oredict.
     */
    @Deprecated
    boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

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
     * Adds a Recipe for Fluid Smelting
     */
    @Deprecated
    boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration,
        int aEUt, boolean hidden);

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
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    @Deprecated
    boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    @Deprecated
    boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt,
        boolean hidden);




    /**
     * Adds Pyrolyse Recipe
     *
     * @param aInput       input item stack
     * @param aFluidInput  fluid input
     * @param intCircuit   circuit index
     * @param aOutput      output item stack
     * @param aFluidOutput fluid output
     * @param aDuration    recipe duration
     * @param aEUt         recipe EU/t expenditure
     *
     * @return if the recipe was successfully added
     */
    @Deprecated
    boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput,
        FluidStack aFluidOutput, int aDuration, int aEUt);


    /**
     * Adds Oil Cracking Recipe
     *
     * @param circuitConfig The circuit configuration to control cracking severity
     * @param aInput        The fluid to be cracked
     * @param aInput2       The fluid to catalyze the cracking (typically Hydrogen or Steam)
     * @param aOutput       The cracked fluid
     * @param aDuration     recipe duration
     * @param aEUt          recipe EU/t expenditure
     */
    @Deprecated
    boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput,
        int aDuration, int aEUt);

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
