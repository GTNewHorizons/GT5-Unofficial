package gregtech.api.interfaces.internal;

import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IGT_RecipeAdder {
    /**
     * Adds a FusionreactorRecipe
     * Does not work anymore!
     */
    @Deprecated
    public boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);

    /**
     * Adds a FusionreactorRecipe
     *
     * @param aInput1                        = first Input (not null, and respects StackSize)
     * @param aInput2                        = second Input (not null, and respects StackSize)
     * @param aOutput                        = Output of the Fusion (can be null, and respects StackSize)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aFusionEnergyPerTick           = The EU generated per Tick (can even be negative!)
     * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
     * @return true if the Recipe got added, otherwise false.
     */
    public boolean addFusionReactorRecipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);

    /**
     * Adds a Centrifuge Recipe
     *
     * @param aInput1    must be != null
     * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     */
    public boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration);

    public boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);

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
    public boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);

    public boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom);
    /**
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @return
     */
    public boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1    must be != null
     * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     * @param aEUt       should be > 0
     */
    public boolean addElectrolyzerRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1    must be != null
     * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     * @param aEUt       should be > 0
     */
    public boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     */
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration);

    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput  must be != null
     * @param aDuration must be > 0
     */
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration);

    /**
     * Adds a Chemical Recipe
     * Only use this when the recipe conflicts in MultiBlock!
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    public boolean addChemicalRecipeForBasicMachineOnly(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);


    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration);

    /**
     * Adds Recipes for creating a radically polymerized polymer from a base Material (for example Ethylene -> Polyethylene)
     * @param aBasicMaterial The basic Material
     * @param aBasicMaterialCell The corresponding Cell basic Material
     * @param aPolymer The polymer
     */
    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer);
    
    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUtick);

    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick, boolean aCleanroom);
    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);

    /**
     +     * Adds a Chemical Recipe that only exists in the Large Chemical Reactor
     +     *
     +     * @param aInputs   item inputs
     +     * @param aFluidInputs fluid inputs
     +     * @param aFluidOutputs fluid outputs
     +     * @param aOutputs  item outputs
     +     * @param aDuration must be > 0
     +     * @param aEUtick   must be > 0
     +     * aInputs and aFluidInputs must contain at least one valid input.
     +     * aOutputs and aFluidOutputs must contain at least one valid output.
     +     */

    public boolean addMultiblockChemicalRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick);


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
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);

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
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);

    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1   must be != null if aInput2 == null
     * @param aInput2   must be != null if aInput1 == null
     * @param aCoalAmount must be > 0
     * @param aOutput1  must be != null if aOutput2 == null
     * @param aOutput2  must be != null if aOutput1 == null
     * @param aDuration must be > 0
     */
    public boolean addPrimitiveBlastRecipe(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1, ItemStack aOutput2, int aDuration);

    /**
     * Adds a Canning Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0, 100 ticks is standard.
     * @param aEUt      should be > 0, 1 EU/t is standard.
     */
    public boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    /**
     * Adds an Alloy Smelter Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);

    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, boolean hidden);

    
    /**
     * Adds a CNC-Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addCNCRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2 	must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds an Assembler Recipe
     *
     * @param aInputs   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);

    public boolean addAssemblerRecipe(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);

    public boolean addAssemblerRecipe(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);

    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom);

    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom);
    
    public boolean addAssemblerSpaceRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom);
    
    /**
     * Adds a Circuit Assembler Recipe
     *
     * @param aInputs   must be 1-6 ItemStacks
     * @param aFluidInput 0-1 fluids
     * @param aOutput  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt);
    
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom);
    
    public boolean addCircuitAssemblerSpaceRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom);
    
    /**
     * Adds a Assemblyline Recipe
     *
     * @param aInputs   must be != null, 4-16 inputs
     * @param aFluidInputs 0-4 fluids
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Assemblyline Recipe
     * @param aInputs   elements should be: ItemStack for single item;
     *                                      ItemStack[] for multiple equivalent items;
     *                                      {OreDict, amount} for oredict.
     */
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Forge Hammer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Wiremill Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addWiremillRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Polariser Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addPolarizerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Plate Bending Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addBenderRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Extruder Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Slicer Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    public boolean addSlicerRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);

    /**
     *
     * @param aInput   must be != null
     * @param aFluidInput   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @return
     */
    public boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int aDuration, int aEUt);

    /**
     * Adds an Implosion Compressor Recipe
     *
     * @param aInput1  must be != null
     * @param aInput2  amount of ITNT, should be > 0
     * @param aOutput1 must be != null
     * @param aOutput2 can be null
     */
    public boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2);

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
    public boolean addGrinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4);

    /**
     * Adds a Distillation Tower Recipe
     *
     * @param aInput1  must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    public boolean addDistillationTowerRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt);


    public boolean addSimpleArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, FluidStack aFluidPutput, int[] aChances, int aDuration, int aEUt);


    /**
     * Adds a Distillation Tower Recipe
     */
    public boolean addDistillationRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);

    /**
     * Adds a Lathe Machine Recipe
     */
    public boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    /**
     * Adds a Cutter Recipe
     */
    public boolean addCutterRecipe(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    /**
     * Adds Cutter Recipes with default Lubricants
     */
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);

    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Boxing Recipe
     */
    public boolean addBoxingRecipe(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration, int aEUt);

    /**
     *
     * @param aInput   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @return
     */
    public boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int aDuration, int aEUt);

    /**
     * Adds an Unboxing Recipe
     */
    public boolean addUnboxingRecipe(ItemStack aFullBox, ItemStack aContainedItem, ItemStack aEmptyBox, int aDuration, int aEUt);

    /**
     * Adds a Vacuum Freezer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     */
    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration);

    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);

    /**
     * Adds a Fuel for My Generators
     *
     * @param aInput1  must be != null
     * @param aOutput1 can be null
     * @param aEU      EU per MilliBucket. If no Liquid Form of this Container is available, then it will give you EU*1000 per Item.
     * @param aType    0 = Diesel; 1 = Gas Turbine; 2 = Thermal; 3 = Dense Fluid; 4 = Plasma; 5 = Magic; And if something is unclear or missing, then look at the GT_Recipe-Class
     */
    public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType);

    /**
     * Adds an Amplifier Recipe for the Amplifabricator
     */
    public boolean addAmplifier(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted);

    /**
     * Adds a Recipe for the Brewing Machine (intentionally limited to Fluid IDs)
     */
    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden);

    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt, boolean aHidden);

    public boolean addBrewingRecipeCustom(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);

    /**
     * Adds a Recipe for the Fermenter
     */
    public boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden);

    /**
     * Adds a Recipe for the Fluid Heater
     */
    public boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Distillery
     */
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden);

    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);

    public boolean addDistilleryRecipe(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden);

    public boolean addDistilleryRecipe(int aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);
    
    /**
     * Adds a Recipe for the Fluid Solidifier
     */
    public boolean addFluidSolidifierRecipe(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for Fluid Smelting
     */
    public boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt);
   
    /**
     * Adds a Recipe for Fluid Smelting
     */
    public boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt, boolean hidden);

    /**
     * Adds a Recipe for Fluid Extraction
     */
    public boolean addFluidExtractionRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Fluid Canner
     */
    public boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput);

    /**
     * Adds a Recipe for the Chemical Bath
     */
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Electromagnetic Separator
     */
    public boolean addElectromagneticSeparatorRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Extractor
     */
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Printer
     */
    public boolean addPrinterRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Autoclave
     */
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Autoclave
     */
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);

    public boolean addAutoclaveSpaceRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Recipe for the Mixer
     * @param itemStack 
     * @param ni 
     */
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);

    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);
    /**
     * Adds a Recipe for the Laser Engraver
     */
    public boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt);
    /**
     * Adds a Recipe for the Laser Engraver
     */
    public boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt, boolean aCleanroom);

    /**
     * Adds a Recipe for the Forming Press
     */
    public boolean addFormingPressRecipe(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Sifter. (up to 9 Outputs)
     */
    public boolean addSifterRecipe(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    public boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    public boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden);

    
    /**
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    public boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);

    /**
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    public boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden);

    /**
     * Adds a Recipe for the GT Industrial Pulveriser. (up to 4 Outputs)
     * @param i 
     */
    public boolean addIndustrialPulverizerRecipe(ItemStack aInput, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    
    public boolean addFlotationUnitRecipe(ItemStack aInput, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);
    
    public boolean addMultiblockCentrifugeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    
    public boolean addMultiblockElectrolyzerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    
	public boolean addTesseractRecipe(ItemStack aInput, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);
    
    /**
     * Adds a Distillation Tower Recipe
     * Every Fluid also gets separate distillation recipes
     *
     * @param aInput1  must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    public boolean addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt);

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
    public boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput, FluidStack aFluidOutput, int aDuration, int aEUt);

    /**
     * Adds Oil Cracking Recipe
     *
     * @param aInput
     * @param aOutput
     * @param aDuration
     * @param aEUt
     */
    @Deprecated
    public boolean addCrackingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);

    /**
     * Adds Oil Cracking Recipe
     *
     * @param circuitConfig The circuit configuration to control cracking severity
     * @param aInput The fluid to be cracked
     * @param aInput2 The fluid to catalyze the cracking (typically Hydrogen or Steam)
     * @param aOutput The cracked fluid
     * @param aDuration
     * @param aEUt
     */
    public boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput, int aDuration, int aEUt);

    /**
     * Adds a Sound to the Sonictron9001
     * you should NOT call this in the preInit-Phase!
     *
     * @param aItemStack = The Item you want to display for this Sound
     * @param aSoundName = The Name of the Sound in the resources/newsound-folder like Vanillasounds
     * @return true if the Sound got added, otherwise false.
     */
    public boolean addSonictronSound(ItemStack aItemStack, String aSoundName);

    /**
     * Returns whether the recipes added are currently being marked as deprecated.
     * Currently only affects Chemical Reactor Recipes.
     */
	public boolean isAddingDeprecatedRecipes();

	/**
	 * Sets whether the recipes added from this point on should be marked as deprecated or not.
     * Currently only affects Chemical Reactor Recipes.
	 */
	public void setIsAddingDeprecatedRecipes(boolean isAddingDeprecatedRecipes);

}
