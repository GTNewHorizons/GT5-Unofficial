package gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;

public interface IGT_RecipeAdder {

    /**
     * Adds a FusionreactorRecipe Does not work anymore!
     */

    @Deprecated
    boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aFusionDurationInTicks,
        int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);

    /**
     * Adds a FusionreactorRecipe
     *
     * @param aInput1                        = first Input (not null, and respects StackSize)
     * @param aInput2                        = second Input (not null, and respects StackSize)
     * @param aOutput1                       = Output of the Fusion (can be null, and respects StackSize)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aFusionEnergyPerTick           = The EU generated per Tick (can even be negative!)
     * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
     * @return true if the Recipe got added, otherwise false.
     */

    @Deprecated
    boolean addFusionReactorRecipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1,
        int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);

    /**
     * Adds a Fusion Reactor Recipe
     *
     * @param FluidOutputArray               : Array of input fluids. Up to 16.
     * @param FluidOutputArray               : Array of output fluids. Up to 16.
     * @param aFusionDurationInTicks         : How many ticks the Fusion lasts (must be > 0).
     * @param aFusionEnergyPerTick           : The EU consumed per tick to keep the reaction going.
     * @param aEnergyNeededForStartingFusion : EU needed to initialize the fusion reaction. (must be >= 0).
     * @return true if the recipe got added, otherwise false.
     */

    @Deprecated
    boolean addFusionReactorRecipe(FluidStack[] FluidInputArray, FluidStack[] FluidOutputArray,
        int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);

    /**
     * Adds a Centrifuge Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aOutput3  can be null
     * @param aOutput4  can be null
     * @param aDuration must be > 0
     */

    @Deprecated
    boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration);

    @Deprecated
    boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);

    /**
     * Adds a Centrifuge Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aOutput3  can be null
     * @param aOutput4  can be null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5,
        ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);

    @Deprecated
    boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5,
        ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @return
     */

    @Deprecated
    boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aOutput3  can be null
     * @param aOutput4  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addElectrolyzerRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aOutput3  can be null
     * @param aOutput4  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5,
        ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration);

    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput, int aDuration);

    /**
     * Adds a Chemical Recipe Only use this when the recipe conflicts in MultiBlock!
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addChemicalRecipeForBasicMachineOnly(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput, ItemStack aOutput2, int aDuration);

    /**
     * Adds Recipes for creating a radically polymerized polymer from a base Material (for example Ethylene ->
     * Polyethylene)
     *
     * @param aBasicMaterial     The basic Material
     * @param aBasicMaterialCell The corresponding Cell basic Material
     * @param aPolymer           The polymer
     */
    void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput, int aDuration, int aEUtick);

    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick, boolean aCleanroom);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    @Deprecated
    boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);

    /**
     * Adds a Chemical Recipe that only exists in the Large Chemical Reactor
     *
     * @param aInputs       item inputs
     * @param aFluidInputs  fluid inputs
     * @param aFluidOutputs fluid outputs
     * @param aOutputs      item outputs
     * @param aDuration     must be > 0
     * @param aEUtick       must be > 0 <br>
     *                      aInputs and aFluidInputs must contain at least one valid input. <br>
     *                      aOutputs and aFluidOutputs must contain at least one valid output.
     *
     */
    @Deprecated
    boolean addMultiblockChemicalRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
        ItemStack[] aOutputs, int aDuration, int aEUtick);

    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @param aLevel    should be > 0 is the minimum Heat Level needed for this Recipe
     */
    @Deprecated
    boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration,
        int aEUt, int aLevel);

    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @param aLevel    should be > 0 is the minimum Heat Level needed for this Recipe
     */
    @Deprecated
    boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);

    @Deprecated
    boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt, int aLevel);

    /**
     * Adds a Plasma Forge Recipe
     *
     * @param ItemInputArray   Array of input items.
     * @param FluidInputArray  Array of output items.
     * @param OutputItemArray  Array of input fluids.
     * @param FluidOutputArray Array of output items.
     * @param aDuration        Must be > 0. Duration in ticks.
     * @param aEUt             Should be > 0. EU/t.
     * @param coil_heat_level  Should be > 0. Heat of the coils used.
     */
    @Deprecated
    boolean addPlasmaForgeRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] OutputItemArray,
        FluidStack[] FluidOutputArray, int aDuration, int aEUt, int coil_heat_level);

    @Deprecated
    boolean addPrimitiveBlastRecipe(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1,
        ItemStack aOutput2, int aDuration);

    /**
     * Adds a Canning Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0, 100 ticks is standard.
     * @param aEUt      should be > 0, 1 EU/t is standard.
     */
    @Deprecated
    boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration,
        int aEUt);

    /**
     * Adds an Alloy Smelter Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt,
        boolean hidden);

    /**
     * Adds a CNC-Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addCNCRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

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
    boolean addAssemblerRecipe(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput,
        ItemStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addAssemblerRecipe(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput,
        ItemStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt,
        boolean aCleanroom);

    /**
     * Adds an Circuit Assembler Recipe
     *
     * @param aInputs     must be 1-6 ItemStacks
     * @param aFluidInput 0-1 fluids
     * @param aOutput     must be != null
     * @param aDuration   must be > 0
     * @param aEUt        should be > 0
     */
    @Deprecated
    boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration,
        int aEUt);

    @Deprecated
    boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration,
        int aEUt, boolean aCleanroom);

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

    // Allows fluids as well as multiple items.
    @Deprecated
    boolean addForgeHammerRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] ItemOutputArray,
        FluidStack[] FluidOutputArray, int aDuration, int aEUt);

    /**
     * Adds a Wiremill Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addWiremillRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    @Deprecated
    boolean addWiremillRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Polariser Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addPolarizerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Plate Bending Machine Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addBenderRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    @Deprecated
    boolean addBenderRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt);

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
     * Adds a Slicer Machine Recipe
     *
     * @param aInput    must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    @Deprecated
    boolean addSlicerRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * @param aInput      must be != null
     * @param aFluidInput must be != null
     * @param aOutput1    must be != null
     * @param aDuration   must be > 0
     * @param aEUt        should be > 0
     * @return
     */
    @Deprecated
    boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int aDuration, int aEUt);

    @Deprecated
    boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds an Implosion Compressor Recipe
     *
     * @param aInput1  must be != null
     * @param aInput2  amount of ITNT, should be > 0
     * @param aOutput1 must be != null
     * @param aOutput2 can be null
     */
    @Deprecated
    boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2);

    /**
     * Adds a Grinder Recipe
     *
     * @param aInput1  must be != null
     * @param aInput2  id for the Cell needed for this Recipe
     * @param aOutput1 must be != null
     * @param aOutput2 can be null
     * @param aOutput3 can be null
     * @param aOutput4 can be null
     */
    @Deprecated
    boolean addGrinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4);

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

    @Deprecated
    boolean addDistillationTowerRecipe(FluidStack aInput, ItemStack[] aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt);

    @Deprecated
    boolean addSimpleArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances,
        int aDuration, int aEUt);

    @Deprecated
    boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances,
        int aDuration, int aEUt);

    @Deprecated
    boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        FluidStack aFluidPutput, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Distillation Tower Recipe
     */
    @Deprecated
    boolean addDistillationRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);

    /**
     * Adds a Lathe Machine Recipe
     */
    @Deprecated
    boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    /**
     * Adds a Cutter Recipe
     */
    @Deprecated
    boolean addCutterRecipe(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt);

    /**
     * Adds Cutter Recipes with default Lubricants
     */
    @Deprecated
    boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    @Deprecated
    boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt,
        boolean aCleanroom);

    @Deprecated
    boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration,
        int aEUt);

    @Deprecated
    boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration,
        int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial);

    @Deprecated
    boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Boxing Recipe
     */
    @Deprecated
    boolean addBoxingRecipe(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration, int aEUt);

    /**
     * @param aInput    must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @return
     */
    @Deprecated
    boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        int aDuration, int aEUt);

    @Deprecated
    boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        int[] aChances, int aDuration, int aEUt);

    /**
     * Adds an Unboxing Recipe
     */
    @Deprecated
    boolean addUnboxingRecipe(ItemStack aFullBox, ItemStack aContainedItem, ItemStack aEmptyBox, int aDuration,
        int aEUt);

    /**
     * Adds a Vacuum Freezer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     */
    @Deprecated
    boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration);

    @Deprecated
    boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addVacuumFreezerRecipe(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt);

    @Deprecated
    boolean addVacuumFreezerRecipe(ItemStack[] aItemInput, FluidStack[] aFluidInput, ItemStack[] aItemOutput,
        FluidStack[] aFluidOutput, int aDuration, int aEUt);

    /**
     * Adds a Fuel for My Generators
     *
     * @param aInput1  must be != null
     * @param aOutput1 can be null
     * @param aEU      EU per MilliBucket. If no Liquid Form of this Container is available, then it will give you
     *                 EU*1000 per Item.
     * @param aType    0 = Diesel; 1 = Gas Turbine; 2 = Thermal; 3 = Dense Fluid; 4 = Plasma; 5 = Magic; And if
     *                 something is unclear or missing, then look at the GT_Recipe-Class
     */
    @Deprecated
    boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType);

    /**
     * Adds an Amplifier Recipe for the Amplifabricator
     */
    @Deprecated
    boolean addAmplifier(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted);

    /**
     * Adds a Recipe for the Brewing Machine (intentionally limited to Fluid IDs)
     */
    @Deprecated
    boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden);

    @Deprecated
    boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt,
        boolean aHidden);

    @Deprecated
    boolean addBrewingRecipeCustom(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden);

    /**
     * Adds a Recipe for the Fermenter
     */
    @Deprecated
    boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden);

    @Deprecated
    boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUT, boolean aHidden);

    /**
     * Adds a Recipe for the Fluid Heater
     */
    @Deprecated
    boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aOutput, int aDuration, int aEUt);

    @Deprecated
    boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Distillery
     */
    @Deprecated
    boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput,
        int aDuration, int aEUt, boolean aHidden);

    @Deprecated
    boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt,
        boolean aHidden);

    @Deprecated
    boolean addDistilleryRecipe(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput,
        int aDuration, int aEUt, boolean aHidden);

    @Deprecated
    boolean addDistilleryRecipe(int aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt,
        boolean aHidden);

    /**
     * Adds a Recipe for the Fluid Solidifier
     */
    @Deprecated
    boolean addFluidSolidifierRecipe(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Fluid Solidifier
     */
    @Deprecated
    boolean addFluidSolidifierRecipe(final ItemStack[] itemInputs, final FluidStack[] fluidInputs,
        final ItemStack[] itemOutputs, final FluidStack[] fluidOutputs, final int EUPerTick,
        final int aDurationInTicks);

    /**
     * Adds a Recipe for Fluid Smelting
     */
    @Deprecated
    boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration,
        int aEUt);

    /**
     * Adds a Recipe for Fluid Smelting
     */
    @Deprecated
    boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration,
        int aEUt, boolean hidden);

    /**
     * Adds a Recipe for Fluid Extraction
     */
    @Deprecated
    boolean addFluidExtractionRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance,
        int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Fluid Canner
     */
    @Deprecated
    boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput);

    @Deprecated
    boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput,
        int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Chemical Bath
     */
    @Deprecated
    boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

    @Deprecated
    boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

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
     * Adds a Recipe for the Printer
     */
    @Deprecated
    boolean addPrinterRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput,
        int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Autoclave
     */
    @Deprecated
    boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration,
        int aEUt);

    @Deprecated
    boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration,
        int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAutoclaveSpaceRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration,
        int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAutoclaveSpaceRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput,
        int aChance, int aDuration, int aEUt, boolean aCleanroom);

    @Deprecated
    boolean addAutoclave4Recipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean aCleanroom);

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

    @Deprecated
    boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt);

    // Use me only from now on!
    @Deprecated
    boolean addMixerRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] ItemOutputArray,
        FluidStack[] FluidOutputArray, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Laser Engraver.
     */
    @Deprecated
    boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration,
        int aEUt);

    /**
     * Adds a Recipe for the Laser Engraver.
     */
    @Deprecated
    boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration,
        int aEUt, boolean aCleanroom);

    /**
     * Adds a Generalised Laser Engraver Recipe.
     *
     * @param ItemInputArray   Array of input items.
     * @param FluidInputArray  Array of output items.
     * @param OutputItemArray  Array of input fluids.
     * @param FluidOutputArray Array of output items.
     * @param aDuration        Must be > 0. Duration in ticks.
     * @param aEUt             Should be > 0. EU/t.
     * @param aCleanroom       Boolean for usage of cleanroom in recipe.
     */
    @Deprecated
    boolean addLaserEngraverRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] OutputItemArray, FluidStack[] FluidOutputArray, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Recipe for the Forming Press
     */
    @Deprecated
    boolean addFormingPressRecipe(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem, int aDuration,
        int aEUt);

    // Allows more than 2 inputs and multiple outputs
    @Deprecated
    boolean addFormingPressRecipe(ItemStack[] ItemInputArray, ItemStack[] OutputItemArray, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Sifter. (up to 9 Outputs)
     */
    @Deprecated
    boolean addSifterRecipe(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Generalised Sifter Recipe.
     *
     * @param ItemInputArray   Array of input items.
     * @param FluidInputArray  Array of output items.
     * @param OutputItemArray  Array of input fluids.
     * @param FluidOutputArray Array of output items.
     * @param aChances         Array of output chances.
     * @param aDuration        Must be > 0. Duration in ticks.
     * @param aEUt             Should be > 0. EU/t.
     * @param aCleanroom       Boolean for usage of cleanroom in recipe.
     */
    @Deprecated
    boolean addSifterRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] OutputItemArray,
        FluidStack[] FluidOutputArray, int[] aChances, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    @Deprecated
    boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    @Deprecated
    boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt,
        boolean hidden);

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
     * Adds a Distillation Tower Recipe Every Fluid also gets separate distillation recipes
     *
     * @param aInput   must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    @Deprecated
    boolean addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration,
        int aEUt);

    @Deprecated
    boolean addUniversalDistillationRecipewithCircuit(FluidStack aInput, ItemStack[] aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt);

    /**
     * Adds Pyrolyse Recipe
     *
     * @param aInput
     * @param intCircuit
     * @param aOutput
     * @param aFluidOutput
     * @param aDuration
     * @param aEUt
     */
    @Deprecated
    boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput,
        FluidStack aFluidOutput, int aDuration, int aEUt);

    /**
     * Adds Oil Cracking Recipe
     *
     * @param aInput
     * @param aOutput
     * @param aDuration
     * @param aEUt
     */
    @Deprecated
    boolean addCrackingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);

    /**
     * Adds Oil Cracking Recipe
     *
     * @param circuitConfig The circuit configuration to control cracking severity
     * @param aInput        The fluid to be cracked
     * @param aInput2       The fluid to catalyze the cracking (typically Hydrogen or Steam)
     * @param aOutput       The cracked fluid
     * @param aDuration
     * @param aEUt
     */
    @Deprecated
    boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput,
        int aDuration, int aEUt);

    /**
     * Adds a Sound to the Sonictron9001 you should NOT call this in the preInit-Phase!
     *
     * @param aItemStack = The Item you want to display for this Sound
     * @param aSoundName = The Name of the Sound in the resources/newsound-folder like Vanillasounds
     * @return true if the Sound got added, otherwise false.
     */
    @Deprecated
    boolean addSonictronSound(ItemStack aItemStack, String aSoundName);

    @Deprecated
    boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt);

    /**
     * Add a Nano Forge Recipe. The Nano Forge's main use is to make nanites/nanorobots. Tier 1 Nano Forge - Can make
     * partly biological, partly metal nanites TIer 2 Nano Forge - Can make mostly metal nanites with some biological
     * aspects TIer 3 Nano Forge - Can make nanites entierly out of metal
     *
     * @param aInputs       = must not be null
     * @param aFluidInputs  = can be null
     * @param aOutputs      = must not be null, the nanite or other output
     * @param aFluidOutputs = can be null
     * @param aChances      = can be null
     * @param aDuration
     * @param aEUt
     * @param aSpecialValue = defines the tier of nano forge required.
     *
     */
    @Deprecated
    boolean addNanoForgeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int[] aChances, int aDuration, int aEUt, int aSpecialValue);

    /**
     * Add a Board Manufacturer Recipe. The Board Manufacturer's main use is to make the circuit boards needed to make
     * circuits.
     *
     * @param aInputs       = must not be null
     * @param aFluidInputs  = must not be null
     * @param aOutputs      = must not be null
     * @param aDuration
     * @param aEUt
     * @param aSpecialValue = defines the tier of the board manufacturer required.
     */
    @Deprecated
    boolean addPCBFactoryRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs, int aDuration,
        int aEUt, int aSpecialValue);

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
